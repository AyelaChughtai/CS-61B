package gitlet;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/** Commit class for Gitlet, implements serializable interface.
 *  @author Ayela Chughtai
 */
@SuppressWarnings("ALL")
public class Commit implements Serializable {

    /** Constructor of the commit class.
     * @param blobs Hashmap of file names to their blobs/contents.
     * @param branch Branch to which the commit is being made.
     * @param logMessage String of a message for the commit.
     * @param parentId String of parent commitId.
     */
    public Commit(String logMessage, String branch, HashMap<String, Blob> blobs,
                  String parentId) {
        _logMessage = logMessage;
        _dateTime = setTimeStamp();
        _branch = branch;
        _blobs = blobs;
        _parentId = parentId;
        _commitId = Utils.sha1('c' + _logMessage + _dateTime + _branch);
    }

    /**
     * Initial constructor of the commit class.
     */
    public Commit() {
        _logMessage = "initial commit";
        _dateTime = "Wed Dec 31 16:00:00 1969 -0800";
        _branch = "master";
        _blobs = new HashMap<String, Blob>();
        _commitId = Utils.sha1('c' + _logMessage + _dateTime + _branch);
        _parentId = "";
    }

    /** Sets date and time of the commit.
     * @return date and time of commit as string. */
    public String setTimeStamp() {
        ZonedDateTime dateTime = ZonedDateTime.now();
        DateTimeFormatter dateTimeFormat =
                DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy");
        return dateTime.format(dateTimeFormat) + " -0800";
    }

    /** Returns the ID of the blob. */
    public String getCommitId() {
        return _commitId;
    }

    /** Returns the ID of the blob. */
    public String getParentId() {
        return _parentId;
    }

    /** Returns the date, day and time of the commit. */
    public String getDateTime() {
        return _dateTime;
    }

    /** Returns a hashmap of file names to their blobs/contents
     * in the commit. */
    public HashMap<String, Blob> getBlobs() {
        return _blobs;
    }

    /** Returns the branch in which the commit was made. */
    public String getBranch() {
        return _branch;
    }

    /** Returns the branch in which the commit was made. */
    public String getLogMessage() {
        return _logMessage;
    }

    /** Returns the blob of a corresponding file name.
     * @param fileName string name of file containing blob. */
    public Blob getBlob(String fileName) {
        return _blobs.get(fileName);
    }

    /** String of commitId.*/
    private String _commitId;

    /** String of parent commitId.*/
    private String _parentId;

    /** Hashmap of file names to their blobs/contents.*/
    private HashMap<String, Blob> _blobs;

    /** Branch to which the commit is being made.*/
    private String _branch;

    /** String of the date, day and time of commit.*/
    private String _dateTime;

    /** String of a log message for the commit.*/
    private String _logMessage;

}
