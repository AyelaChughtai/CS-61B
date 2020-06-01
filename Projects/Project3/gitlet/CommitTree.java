package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/** CommitTree class for Gitlet, implements serializable interface.
 *  @author Ayela Chughtai
 */
@SuppressWarnings("ALL")
public class CommitTree implements Serializable {

    /** Constructor of the commit tree class. */
    public CommitTree() {
        _branches = new HashMap<>();
        _allCommits = new HashMap<>();
        Commit initialCommit = new Commit();
        _branches.put("master", initialCommit);
        _allCommits.put(initialCommit.getCommitId(), initialCommit);
        _currentBranch = "master";
        _lastCommit = initialCommit;
        _splitPoints = new HashMap<>();
    }

    /** Returns the blob of a corresponding file name.
     * @param fileName is name of the file. */
    public Blob getLatestFiles(String fileName) {
        return _lastCommit.getBlob(fileName);
    }

    /** Returns string pointer to the name of the current branch. */
    public String getCurrentBranch() {
        return _currentBranch;
    }

    /** Sets string pointer to the name of the new current branch.
     * @param branchName . */
    public void setCurrentBranch(String branchName) {
        _currentBranch = branchName;
    }

    /** Returns hashMap of branch names to commit heads in those branches. */
    public HashMap<String, Commit> getBranches() {
        return _branches;
    }

    /** Returns branch head commit for a given branch.
     * @param branchName . */
    public Commit getBranchHead(String branchName) {
        return _branches.get(branchName);
    }

    /** Returns pointer to latest commit. */
    public Commit getLastCommit() {
        return _lastCommit;
    }

    /** Sets pointer to latest commit.
     * @param commit . */
    public void setLastCommit(Commit commit) {
        _lastCommit = commit;
    }

    /** Returns commit object for a given ID.
     * @param commitId . */
    public Commit getCommit(String commitId) {
        return _allCommits.get(commitId);
    }

    /** Returns Hashmap of all commits, mapping commitIds to commit objects. */
    public HashMap<String, Commit> getAllCommits() {
        return _allCommits;
    }

    /** Returns Hashmap of all branch names to split points. */
    public HashMap<String, Commit> getSplitPoints() {
        return _splitPoints;
    }

    /** Merges changes made by this branch into the other.
     * @param branchName name of branch to be merged. */
    public void merge(String branchName) {
        String currentBranch = _currentBranch;
        Commit splitPoint = _splitPoints.get(branchName + currentBranch);
        Commit currHead = getBranchHead(currentBranch);
        Commit branchHead = getBranchHead(branchName);
        for (String fileName: splitPoint.getBlobs().keySet()) {
            if (!isRemoved(branchHead, fileName)
                    && !isRemoved(currHead, fileName)) {
                Blob given = branchHead.getBlob(fileName);
                Blob split = splitPoint.getBlob(fileName);
                Blob curr = currHead.getBlob(fileName);
                if (isModified(given, split) && !isModified(curr, split)) {
                    Utils.join(System.getProperty("user.dir"),
                            fileName).delete();
                    Utils.writeContents(Utils.join(
                            System.getProperty("user.dir"),
                            fileName), given.getFileContentsString());
                    Main.getStagingArea().add(fileName, given);
                } else if (isModified(given, split) && isModified(curr, split)
                        && isModified(given, curr)) {
                    System.out.println("Encountered a merge conflict.");
                    Utils.join(System.getProperty("user.dir"),
                            fileName).delete();
                    String newContents = "<<<<<<< HEAD" + "\n"
                            + curr.getFileContentsString() + "\n" + "======="
                            + given.getFileContentsString();
                    Utils.writeContents(Utils.join(System.getProperty(
                            "user.dir"), fileName), newContents);
                }
            } else if (isRemoved(branchHead, fileName)
                    && !isRemoved(currHead, fileName)) {
                Blob split = splitPoint.getBlob(fileName);
                Blob curr = currHead.getBlob(fileName);
                if (!isModified(curr, split)) {
                    Main.getStagingArea().remove(fileName, curr);
                }
                Utils.join(System.getProperty("user.dir"),
                        fileName).delete();
            }
        }
        for (String fileName: branchHead.getBlobs().keySet()) {
            if (!splitPoint.getBlobs().containsKey(fileName)
                    && !currHead.getBlobs().containsKey(fileName)) {
                Blob given = branchHead.getBlob(fileName);
                Utils.join(System.getProperty("user.dir"), fileName).delete();
                Utils.writeContents(Utils.join(System.getProperty("user.dir"),
                        fileName), given.getFileContentsString());
                Main.getStagingArea().add(fileName, given);
            }
        }
    }

    /** Checks if content of blobs is different.
     * @param given blob.
     * @param splitPoint blob.
     * @return boolean out. */
    private static boolean isModified(Blob given, Blob splitPoint) {
        boolean out = false;
        if (splitPoint.getBlobId().equals(given.getBlobId())) {
            out = true;
        }
        return out;
    }

    /** Checks if blob is still there.
     * @param commit commit.
     * @param fileName string.
     * @return boolean out.*/
    private static boolean isRemoved(Commit commit, String fileName) {
        boolean out = false;
        if (!commit.getBlobs().containsKey(fileName)) {
            out = true;
        }
        return out;
    }

    /** Saves a snapshot of current files and staging area
     * so they can be restored at a later time.
     * @param logMessage string of message for commit.
     * @param added files staged for commit.
     * @param removing files staged for removal.
     * @param branchHead commit.
     * @param currHead commit. */
    public void mergeCommit(String logMessage, HashMap<String, Blob> added,
                            HashMap<String, Blob> removing, Commit currHead,
                            Commit branchHead) {
        HashMap<String, Blob> newCommitBlobs = new HashMap<>();
        if (_lastCommit.getLogMessage().equals("initial commit")) {
            for (String fileName : added.keySet()) {
                newCommitBlobs.put(fileName, added.get(fileName));
                Main.getStagingArea().getTracked().put(fileName,
                        added.get(fileName));
            }
        } else {
            for (String fileName : added.keySet()) {
                if (_lastCommit.getBlobs().containsKey(fileName)) {
                    if (_lastCommit.getBlobs().get(fileName).getBlobId(
                    ).equals(added.get(fileName).getBlobId())) {
                        newCommitBlobs.put(fileName,
                                _lastCommit.getBlobs().get(fileName));
                    } else {
                        newCommitBlobs.put(fileName, added.get(fileName));
                    }
                } else {
                    newCommitBlobs.put(fileName, added.get(fileName));
                    Main.getStagingArea().getTracked().put(fileName,
                            added.get(fileName));
                }
            }
            for (String fileName : _lastCommit.getBlobs().keySet()) {
                if (!added.containsKey(fileName)) {
                    newCommitBlobs.put(fileName,
                            _lastCommit.getBlobs().get(fileName));
                }
            }
        }
        for (String fileName:  removing.keySet()) {
            if (newCommitBlobs.containsKey(fileName)) {
                newCommitBlobs.remove(fileName);
                Main.getStagingArea().getRemoved().put(fileName,
                        removing.get(fileName));
                Main.getStagingArea().getRemoving().remove(fileName);
            }
            Main.getStagingArea().getTracked().remove(fileName);
        }
        Commit newCommit = new Commit(logMessage, _currentBranch,
                newCommitBlobs, currHead.getCommitId()
                + " " + branchHead.getCommitId());
        _currentBranch = newCommit.getBranch();
        _lastCommit = newCommit;
        _branches.put(_currentBranch, newCommit);
        _allCommits.put(newCommit.getCommitId(), newCommit);
        Main.getStagingArea().getAdded().clear();
        Main.getStagingArea().getRemoving().clear();
    }

    /** Saves a snapshot of current files and staging area
     * so they can be restored at a later time.
     * @param logMessage string of message for commit.
     * @param added files staged for commit.
     * @param removing files staged for removal. */
    public void commit(String logMessage, HashMap<String, Blob> added,
                              HashMap<String, Blob> removing) {
        HashMap<String, Blob> newCommitBlobs = new HashMap<>();
        if (_lastCommit.getLogMessage().equals("initial commit")) {
            for (String fileName : added.keySet()) {
                newCommitBlobs.put(fileName, added.get(fileName));
                Main.getStagingArea().getTracked().put(fileName,
                        added.get(fileName));
            }
        } else {
            for (String fileName : added.keySet()) {
                if (_lastCommit.getBlobs().containsKey(fileName)) {
                    if (_lastCommit.getBlobs().get(fileName).getBlobId(
                    ).equals(added.get(fileName).getBlobId())) {
                        newCommitBlobs.put(fileName,
                                _lastCommit.getBlobs().get(fileName));
                    } else {
                        newCommitBlobs.put(fileName, added.get(fileName));
                    }
                } else {
                    newCommitBlobs.put(fileName, added.get(fileName));
                    Main.getStagingArea().getTracked().put(fileName,
                            added.get(fileName));
                }
            }
            for (String fileName : _lastCommit.getBlobs().keySet()) {
                if (!added.containsKey(fileName)) {
                    newCommitBlobs.put(fileName,
                            _lastCommit.getBlobs().get(fileName));
                }
            }
        }
        for (String fileName:  removing.keySet()) {
            if (newCommitBlobs.containsKey(fileName)) {
                newCommitBlobs.remove(fileName);
                Main.getStagingArea().getRemoved().put(fileName,
                        removing.get(fileName));
                Main.getStagingArea().getRemoving().remove(fileName);
            }
            Main.getStagingArea().getTracked().remove(fileName);
        }
        Commit newCommit =  new Commit(logMessage, _currentBranch,
                    newCommitBlobs, _lastCommit.getCommitId());
        _currentBranch = newCommit.getBranch();
        _lastCommit = newCommit;
        _branches.put(_currentBranch, newCommit);
        _allCommits.put(newCommit.getCommitId(), newCommit);
        Main.getStagingArea().getAdded().clear();
        Main.getStagingArea().getRemoving().clear();
    }

    /** Creates a new branch with the given name and points it
     * at the current head node.
     * @param branchName name of branch to be made. */
    public void branch(String branchName) {
        Commit head = getBranchHead(_currentBranch);
        _branches.put(branchName, head);
        _splitPoints.put(branchName + _currentBranch, head);
        _splitPoints.put(_currentBranch + branchName, head);
    }

    /** Checks out all files tracked by given commit,
     * removes if not present, moves pointer to this commit node.
     * Clears staging area.
     * @param commitId ID of commit to be reset. */
    public void reset(String commitId) {
        List<String> workingDirectoryFiles = Utils.plainFilenamesIn
                (System.getProperty("user.dir"));
        Commit resetTo  = getCommit(commitId);
        HashMap<String, Blob> resetTracked = new HashMap<>();
        for (String fileName: resetTo.getBlobs().keySet()) {
            Blob blob = resetTo.getBlob(fileName);
            resetTracked.put(fileName, blob);
            if (workingDirectoryFiles.contains(fileName)) {
                Utils.join(System.getProperty("user.dir"), fileName).delete();
                Utils.writeContents(Utils.join(System.getProperty("user.dir"),
                        fileName), blob.getFileContentsString());
            } else {
                Utils.writeContents(Utils.join(System.getProperty("user.dir"),
                        fileName), blob.getFileContentsString());
            }
        }
        for (String fileName: workingDirectoryFiles) {
            if (!resetTo.getBlobs().keySet().contains(fileName)) {
                Utils.join(System.getProperty("user.dir"), fileName).delete();
            }
        }
        _branches.put(_currentBranch, resetTo);
        _lastCommit = resetTo;
        Main.getStagingArea().setTracked(resetTracked);
        Main.getStagingArea().getAdded().clear();
        Main.getStagingArea().getRemoving().clear();
    }

    /** HashMap of branch names to commit heads in those branches. */
    private HashMap<String, Commit> _branches;

    /** String pointer to the name of the current branch. */
    private String _currentBranch;

    /** Pointer to latest commit. */
    private Commit _lastCommit;

    /** Hashmap of all commits, mapping commit IDs to commit objects. */
    private HashMap<String, Commit> _allCommits;

    /** Hashmap of combined branch names to split points. */
    private HashMap<String, Commit> _splitPoints;

}

