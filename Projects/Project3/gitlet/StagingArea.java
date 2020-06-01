package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/** StagingArea class for Gitlet, implements serializable interface.
 *  @author Ayela Chughtai
 */
@SuppressWarnings("ALL")
public class StagingArea implements Serializable {

    /** Constructor of the staging area class.
     */
    public StagingArea() {
        _added = new HashMap<>();
        _removed = new HashMap<>();
        _removing = new HashMap<>();
        _tracked = new HashMap<>();
    }

    /** Returns HashMap<String, Blob> of files
     * staged for commit (add). */
    public HashMap<String, Blob> getAdded() {
        return _added;
    }

    /** Returns HashMap<String, Blob>
     *  of files removed (rm). */
    public HashMap<String, Blob> getRemoved() {
        return _removed;
    }

    /** Returns HashMap<String, Blob>
     *  of files staged for removal (rm). */
    public HashMap<String, Blob> getRemoving() {
        return _removing;
    }

    /** Returns HashMap<String, Blob> of files in wd that are tracked in latest
     * commit. */
    public HashMap<String, Blob> getTracked() {
        return _tracked;
    }

    /** Sets HashMap<String, Blob> of files in wd that are
     * tracked in latest commit.
     * @param tracked .*/
    public void setTracked(HashMap<String, Blob> tracked) {
        _tracked = tracked;
    }

    /** Adds copy of file to staging area, overwriting previously staged files.
            * @param fileName string of name of file to be added.
     * @param blob blob of content of file to be added. */
    public void remove(String fileName, Blob blob) {
        if (_added.containsKey(fileName)) {
            _added.remove(fileName);
        }
        Commit lastCommit = Main.getCommitTree().getLastCommit();
        if (lastCommit.getBlobs().containsKey(fileName)
                || _tracked.containsKey(fileName)) {
            _removing.put(fileName, blob);
            File file = new File(System.getProperty("user.dir"), fileName);
            if (file.exists()) {
                Utils.join(System.getProperty("user.dir"), fileName).delete();

            }
        }
    }

    /** Adds copy of file to staging area, overwriting previously staged files.
     * @param fileName string of name of file to be added.
     * @param blob blob of content of file to be added. */
    public void add(String fileName, Blob blob) {
        if (_added.containsKey(fileName)) {
            if (!_added.get(fileName).getBlobId().equals(blob.getBlobId())) {
                _added.remove(fileName);
                _added.put(fileName, blob);
            }
        } else {
            _added.put(fileName, blob);
        }
    }

    /** Displays what branches exist, files staged for addition/removal,
     * modifications not staged for commit [EC], and untracked files [EC]. */
    public void status() {
        ArrayList<String> sortedBranches = new ArrayList<>(
                Main.getCommitTree().getBranches().keySet());
        Collections.sort(sortedBranches);
        System.out.println("=== Branches ===");
        for (String branch : sortedBranches) {
            if (branch.equals(Main.getCommitTree().getCurrentBranch())) {
                System.out.println("*" + branch);
            } else {
                System.out.println(branch);
            }
        }
        System.out.println();
        ArrayList<String> sortedAdded = new ArrayList<>(
                _added.keySet());
        Collections.sort(sortedAdded);
        System.out.println("=== Staged Files ===");
        for (String fileName : sortedAdded) {
            System.out.println(fileName);
        }
        System.out.println();
        ArrayList<String> sortedRemoving = new ArrayList<>(
                _removing.keySet());
        Collections.sort(sortedRemoving);
        System.out.println("=== Removed Files ===");
        for (String fileName : sortedRemoving) {
            System.out.println(fileName);
        }
        System.out.println();
        ArrayList<String> sortedModified = statusModified();
        Collections.sort(sortedModified);
        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String fileName : sortedModified) {
            System.out.println(fileName);
        }
        System.out.println();
        ArrayList<String> sortedUntracked = statusUntracked();
        Collections.sort(sortedUntracked);
        System.out.println("=== Untracked Files ===");
        for (String fileName : sortedUntracked) {
            System.out.println(fileName);
        }
    }

    /** Finds files in cwd with modifications not staged for commit.
     * @return ArrayList<String> */
    public ArrayList<String> statusModified() {
        Commit lastCommit = Main.getCommitTree().getLastCommit();
        ArrayList<String> mod = new ArrayList<>();
        List<String> workingDirectoryFiles = Utils.plainFilenamesIn
                (System.getProperty("user.dir"));
        assert workingDirectoryFiles != null;
        for (String fileName: workingDirectoryFiles) {
            Blob cwdBlob = new Blob(fileName);
            if (lastCommit.getBlobs().containsKey(fileName)
                    || _tracked.containsKey(fileName)) {
                if (!lastCommit.getBlob(fileName).getBlobId().equals(
                        cwdBlob.getBlobId())) {
                    if (_added.containsKey(fileName)) {
                        if (!_added.get(fileName).getBlobId().equals(
                                cwdBlob.getBlobId())) {
                            mod.add(fileName + " (modified)");
                        }
                    } else {
                        mod.add(fileName + " (modified)");
                    }
                }
            } else if (_added.containsKey(fileName)) {
                if (!_added.get(fileName).getBlobId().equals(
                        cwdBlob.getBlobId())) {
                    mod.add(fileName + " (modified)");
                }
            }
        }

        for (String fileName: _added.keySet()) {
            if (!workingDirectoryFiles.contains(fileName)
                    || _removed.containsKey(fileName)) {
                mod.add(fileName + " (modified)");
            }
            if (!_removing.containsKey(fileName)
                    && (lastCommit.getBlobs().containsKey(fileName)
                    || _tracked.containsKey(fileName))
                    && (!workingDirectoryFiles.contains(fileName)
                    || _removed.containsKey(fileName))) {
                mod.add(fileName + " (modified)");
            }
        }
        for (String fileName: lastCommit.getBlobs().keySet()) {
            if (!workingDirectoryFiles.contains(fileName)
                    && !_removing.containsKey(fileName)
                    && !_added.containsKey(fileName)) {
                mod.add(fileName + " (deleted)");
            }
        }
        return mod;
    }

    /** Finds files in cwd that are untracked.
     * @return Arraylist<String> of file names. */
    public ArrayList<String> statusUntracked() {
        ArrayList<String> untracked = new ArrayList<>();
        List<String> workingDirectoryFiles = Utils.plainFilenamesIn
                (System.getProperty("user.dir"));
        for (String fileName : workingDirectoryFiles) {
            if (!_added.containsKey(fileName)
                    && !_tracked.containsKey(fileName)) {
                untracked.add(fileName);
            } else if (_removing.containsKey(fileName)) {
                untracked.add(fileName);
            }
        }
        return untracked;
    }

    /** HashMap<String, Blob> of files staged for commit (add).*/
    private HashMap<String, Blob> _added;

    /** HashMap<String, Blob> of files removed (rm).*/
    private HashMap<String, Blob> _removed;

    /** HashMap<String, Blob> of files staged for removal (rm).*/
    private HashMap<String, Blob> _removing;

    /** HashMap<String, Blob> of files in wd that are tracked in latest
     * commit.*/
    private HashMap<String, Blob> _tracked;

}

