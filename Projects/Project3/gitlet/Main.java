package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Ayela Chughtai
 */
@SuppressWarnings("ALL")
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        commandError(args);
        String command = args[0];
        switch (command) {
        case "init":
            operandError(args, 1);
            init();
            break;
        case "add":
            initError(); operandError(args, 2);
            add(args[1]);
            break;
        case "commit" :
            initError(); operandError(args, 2);
            commit(args[1]);
            break;
        case "rm" :
            initError(); operandError(args, 2);
            remove(args[1]);
            break;
        case "log" :
            initError(); operandError(args, 1);
            log();
            break;
        case "global-log" :
            initError(); operandError(args, 1);
            globalLog();
            break;
        case "find" :
            initError(); operandError(args, 2);
            find(args[1]);
            break;
        case "status" :
            initError(); operandError(args, 1);
            status();
            break;
        case "checkout" :
            checkOutError(args);
            break;
        case "branch" :
            initError(); operandError(args, 2);
            branch(args[1]);
            break;
        case "rm-branch" :
            initError(); operandError(args, 2);
            removeBranch(args[1]);
            break;
        case "reset" :
            initError(); operandError(args, 2);
            reset(args[1]);
            break;
        case "merge" :
            initError(); operandError(args, 2);
            merge(args[1]);
            break;
        default :
            System.out.println("No command with that name exists.");
            System.exit(0);
        }
    }

    /** Checks that a command has been entered.
     * @param args String[] of args. */
    private static void commandError(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
    }

    /** Checks correct operands for commands.
     * @param args String[] of args.
     * @param argLen Integer length that args need to be*/
    private static void operandError(String[] args, Integer argLen) {
        if (args.length != argLen) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    /** Checks that .gitlet has been initialised.*/
    private static void initError() {
        if (!Utils.join(System.getProperty("user.dir"),
                        ".gitlet").exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    /** Checks correct operands for checkout command.
     * @param args String[] of args. */
    private static void checkOutError(String[] args) {
        initError();
        if (args.length > 4 || args.length < 2) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        } else if (args.length == 3) {
            if (!args[1].equals("--")) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            } else {
                checkout(args[2]);
            }
        } else if (args.length == 4) {
            if (!args[2].equals("--")) {
                System.out.println("Incorrect operands.");
                System.exit(0);
            } else {
                checkout(args[1], args[3]);
            }
        } else {
            checkoutBranch(args[1]);
        }
    }

    /** Reverts cwd to head commit.
     * @param fileName name of file. */
    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    private static void checkout(String fileName) {
        readFiles();
        if (fileName == null) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        if (!_commitTree.getLastCommit().getBlobs().containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else {
            Utils.join(System.getProperty("user.dir"), fileName).delete();
            Blob blob = _commitTree.getLatestFiles(fileName);
            Utils.writeContents(Utils.join(System.getProperty("user.dir"),
                    fileName), blob.getFileContentsString());
        }
    }

    /** Reverts wd to commit at the head of the given branch.
     * @param branchName name of branch. */
    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    private static void checkoutBranch(String branchName) {
        readFiles();
        if (branchName == null) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        if (!_commitTree.getBranches().containsKey(branchName)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }
        if (_commitTree.getCurrentBranch().equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        if (_stagingArea.statusUntracked().size() > 0) {
            System.out.println("There is an untracked file in the way;"
                    + " delete it, or add and commit it first.");
            System.exit(0);
        }
        HashMap<String, Blob> tracked = new HashMap<>();
        Commit branchHead = _commitTree.getBranchHead(branchName);
        for (String fileName : branchHead.getBlobs().keySet()) {
            Utils.join(System.getProperty("user.dir"), fileName).delete();
            Blob blob = branchHead.getBlob(fileName);
            Utils.writeContents(Utils.join(System.getProperty("user.dir"),
                    fileName), blob.getFileContentsString());
            tracked.put(fileName, branchHead.getBlob(fileName));
        }

        List<String> workingDirectoryFiles = Utils.plainFilenamesIn
                (System.getProperty("user.dir"));
        for (String fileName : workingDirectoryFiles) {
            if (!branchHead.getBlobs().containsKey(fileName)) {
                Utils.join(System.getProperty("user.dir"),
                        fileName).delete();
            }
        }
        _stagingArea.setTracked(tracked);
        _commitTree.setCurrentBranch(branchName);
        _commitTree.setLastCommit(branchHead);
        _stagingArea.getAdded().clear();
        _stagingArea.getRemoving().clear();
        writeFiles();
    }

    /** Overwrites file in wd to given commit.
     * @param commitId ID of commit.
     * @param fileName name of file to overwrite. */
    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    private static void checkout(String commitId, String fileName) {
        readFiles();
        if (commitId == null || fileName == null || commitId.length() < 6) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        boolean contains = false;
        String fullId = null;
        for (String commitIds: _commitTree.getAllCommits().keySet()) {
            if (commitIds.contains(commitId)) {
                contains = true;
                fullId = commitIds;
            }
        }
        if (!contains) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        } else if (!_commitTree.getCommit(
                fullId).getBlobs().containsKey(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        } else {
            Utils.join(System.getProperty("user.dir"), fileName).delete();
            Blob blob = _commitTree.getCommit(
                    fullId).getBlob(fileName);
            Utils.writeContents(Utils.join(System.getProperty("user.dir"),
                    fileName), blob.getFileContentsString());
        }
    }

    /** Merges changes made by this branch into the other.
     * @param branchName name of branch to be merged. */
    private static void merge(String branchName) {
        readFiles();
        String currentBranch = _commitTree.getCurrentBranch();
        Commit splitPoint = _commitTree.getSplitPoints().get(
                branchName + currentBranch);
        Commit currHead = _commitTree.getBranchHead(currentBranch);
        Commit branchHead = _commitTree.getBranchHead(branchName);
        if (branchName == null) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        if (branchName.equals(currentBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }
        if (!_commitTree.getBranches().containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (_stagingArea.statusUntracked().size() > 0) {
            System.out.println("There is an untracked file in the way;"
                    + " delete it or add and commit it first");
            System.exit(0);
        }
        if (!_stagingArea.getAdded().isEmpty()
                || !_stagingArea.getRemoving().isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
        }
        if (splitPoint.equals(branchHead)) {
            System.out.println("Given branch is an ancestor of the "
                    + "current branch.");
            System.exit(0);
        }
        if (splitPoint.equals(currHead)) {
            checkout(branchName);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }
        _commitTree.merge(branchName);
        mergeCommit("Merged " + branchName + " into " + currentBranch + ".",
                currHead, branchHead);
        writeFiles();
    }

    /** Saves a snapshot of current files and staging area
     * so they can be restored at a later time.
     * @param logMessage string of message for commit.
     * @param branchHead commit.
     * @param currHead commit.*/
    private static void mergeCommit(String logMessage, Commit currHead,
                                    Commit branchHead) {
        if (logMessage == null) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        if (logMessage.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        } else if (_stagingArea.getAdded().size() == 0
                && _stagingArea.getRemoving().size() == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        _commitTree.mergeCommit(logMessage, _stagingArea.getAdded(),
                _stagingArea.getRemoving(), currHead, branchHead);
        writeFiles();
    }


    /** Checks out all files tracked by given commit,
     * removes if not present, moves pointer to this commit node.
     * Clears staging area.
     * @param commitId ID of commit to be reset. */
    private static void reset(String commitId) {
        readFiles();
        if (commitId == null) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        boolean contains = false;
        String fullId = null;
        for (String commitIds: _commitTree.getAllCommits().keySet()) {
            if (commitIds.contains(commitId)) {
                contains = true;
                fullId = commitIds;
                break;
            }
        }
        if (!contains) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        } else if (_stagingArea.statusUntracked().size() > 0) {
            System.out.println("There is an untracked file in the way;"
                    + " delete it or add and commit it first");
            System.exit(0);
        }
        _commitTree.reset(fullId);
        writeFiles();
    }

    /** Deletes branch with given name without modifying commits.
     * @param branchName name of branch to be removed. */
    private static void removeBranch(String branchName) {
        readFiles();
        if (branchName == null) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        if (!_commitTree.getBranches().containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }
        if (branchName.equals(_commitTree.getCurrentBranch())) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }
        _commitTree.getBranches().remove(branchName);
        writeFiles();
    }

    /** Creates a new branch with the given name and points it
     * at the current head node.
     * @param branchName name of branch to be made. */
    private static void branch(String branchName) {
        readFiles();
        if (branchName == null) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        if (_commitTree.getBranches().containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
        _commitTree.branch(branchName);
        writeFiles();
    }

    /** Displays what branches exist, files staged for addition/removal,
     * modifications not staged for commit [EC], and untracked files [EC]. */
    private static void status() {
        readFiles();
        _stagingArea.status();
    }

    /** Prints commitIds of all commits with that message.
     * @param logMessage string of message for commits to be found. */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void find(String logMessage) {
        readFiles();
        if (logMessage == null) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        ArrayList commitIds = new ArrayList();
        for (String commitId: _commitTree.getAllCommits().keySet()) {
            if (_commitTree.getCommit(commitId).getLogMessage().equals(
                    logMessage)) {
                commitIds.add(commitId);
                System.out.println(commitId);
            }
        }
        if (commitIds.isEmpty()) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
    }

    /** Displays info for all commits ever made in random order. */
    private static void globalLog() {
        readFiles();
        for (String commitId: _commitTree.getAllCommits().keySet()) {
            Commit lastCommit = _commitTree.getCommit(commitId);
            System.out.println("===" + "\n" + "commit "
                    + lastCommit.getCommitId());
            System.out.println("Date: " + lastCommit.getDateTime());
            System.out.println(lastCommit.getLogMessage() + "\n");
        }
    }

    /** Displays commits from head to initial ignoring second parents. */
    private static void log() {
        readFiles();
        Commit lastCommit = _commitTree.getLastCommit();
        while (!lastCommit.getLogMessage().equals("initial commit")) {
            if (lastCommit.getLogMessage().contains("Merged")) {
                System.out.println("===" + "\n" + "commit "
                        + lastCommit.getCommitId());
                String[] parentsIds = lastCommit.getParentId().split(" ");
                System.out.println("Merge: " + parentsIds[0].substring(0, 6)
                        + " " + parentsIds[1].substring(0, 6));
                System.out.println("Date: " + lastCommit.getDateTime());
                System.out.println(lastCommit.getLogMessage() + "\n");
                lastCommit = _commitTree.getAllCommits().get(parentsIds[0]);
            } else {
                System.out.println("===" + "\n" + "commit "
                        + lastCommit.getCommitId());
                System.out.println("Date: " + lastCommit.getDateTime());
                System.out.println(lastCommit.getLogMessage() + "\n");
                lastCommit = _commitTree.getAllCommits().get(
                        lastCommit.getParentId());
            }
        }
        System.out.println("===" + "\n" + "commit "
                + lastCommit.getCommitId());
        System.out.println("Date: " + lastCommit.getDateTime());
        System.out.println(lastCommit.getLogMessage());
    }

    /** Unstage a file staged for commit. If the file is tracked
     * in the current commit,stage it for removal and remove the
     * file from the working directory.
     * @param fileName string of name of file to be removed. */
    private static void remove(String fileName) {
        readFiles();
        if (fileName == null) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        if (!_stagingArea.getAdded().containsKey(fileName)
                && (!_commitTree.getLastCommit().getBlobs().containsKey(
                        fileName)
                || !_stagingArea.getTracked().containsKey(fileName))) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
        File file = new File(System.getProperty("user.dir"), fileName);
        if (file.exists()) {
            Blob toRemoveBlob = new Blob(fileName);
            _stagingArea.remove(fileName, toRemoveBlob);
        } else if (_commitTree.getLastCommit().getBlobs().containsKey(
                fileName)) {
            Blob toRemoveBlob = _commitTree.getLatestFiles(fileName);
            _stagingArea.getRemoving().put(fileName, toRemoveBlob);
        }
        writeFiles();
    }

    /** Saves a snapshot of current files and staging area
     * so they can be restored at a later time.
     * @param logMessage string of message for commit. */
    private static void commit(String logMessage) {
        readFiles();
        if (logMessage == null) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        if (logMessage.equals("")) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        } else if (_stagingArea.getAdded().size() == 0
                && _stagingArea.getRemoving().size() == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
        _commitTree.commit(logMessage, _stagingArea.getAdded(),
                _stagingArea.getRemoving());
        writeFiles();
    }

    /** Adds copy of file to staging area, overwriting previously staged files.
     * @param fileName string of name of file to be added. */
    private static void add(String fileName) {
        readFiles();
        if (fileName == null) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
        File toAdd = new File(fileName);
        if (!toAdd.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        Blob toAddBlob = new Blob(fileName);
        if (_stagingArea.getRemoving().containsKey(fileName)) {
            _stagingArea.getRemoving().remove(fileName);
            if (!toAddBlob.getBlobId().equals(
                    _commitTree.getLatestFiles(fileName).getBlobId())) {
                _stagingArea.getAdded().put(fileName, toAddBlob);
            }
        } else if (_commitTree.getLastCommit() != null
                && _commitTree.getLatestFiles(fileName) != null) {
            if (!_commitTree.getLatestFiles(
                    fileName).getBlobId().equals(toAddBlob.getBlobId())) {
                _stagingArea.add(fileName, toAddBlob);
            } else {
                if (_stagingArea.getAdded().containsKey(fileName)) {
                    _stagingArea.getAdded().remove(fileName);
                }
            }
        } else {
            _stagingArea.add(fileName, toAddBlob);
        }
        writeFiles();
    }

    /** Initialises gitlet and creates a new version control system in
     * the current directory. */
    private static void init() {
        File gitlet = Utils.join(System.getProperty("user.dir"), ".gitlet");
        if (gitlet.exists()) {
            System.out.println("Gitlet version-control system already "
                    + "exists in the current directory.");
            System.exit(0);
        }
        gitlet.mkdir();
        _commitTree = new CommitTree();
        _stagingArea = new StagingArea();
        Utils.writeObject(Utils.join(gitlet, "/CommitTree"), _commitTree);
        Utils.writeObject(Utils.join(gitlet, "/StagingArea"), _stagingArea);
    }

    /** Read the staging area and commit tree objects. */
    private static void readFiles() {
        File ct = new File(".gitlet/CommitTree");
        File sa = new File(".gitlet/StagingArea");
        _commitTree = Utils.readObject(ct, CommitTree.class);
        _stagingArea = Utils.readObject(sa, StagingArea.class);
    }

    /** Write changes to the staging area and commit tree objects. */
    private static void writeFiles() {
        File gitlet = Utils.join(System.getProperty("user.dir"), ".gitlet");
        Utils.join(gitlet, "/StagingArea").delete();
        Utils.writeObject(Utils.join(gitlet, "/StagingArea"), _stagingArea);
        Utils.join(gitlet, "/CommitTree").delete();
        Utils.writeObject(Utils.join(gitlet, "/CommitTree"), _commitTree);
    }

    /** Returns commit tree for gitlet. */
    public static CommitTree getCommitTree() {
        return _commitTree;
    }

    /** Returns staging area for gitlet. */
    public static StagingArea getStagingArea() {
        return _stagingArea;
    }

    /** Commit tree for gitlet. */
    private static CommitTree _commitTree;

    /** Staging Area for gitlet. */
    private static StagingArea _stagingArea;

}
