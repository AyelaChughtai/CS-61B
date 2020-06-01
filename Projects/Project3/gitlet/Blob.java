package gitlet;

import java.io.File;
import java.io.Serializable;

/** Blob class for Gitlet, implements serializable interface.
 *  @author Ayela Chughtai
 */
public class Blob implements Serializable {

    /** Constructor of the blob class.
     * @param fileName is name of the file.
     * */
    public Blob(String fileName) {
        _fileName = fileName;
        _file = new File(_fileName);
        _contents = Utils.readContents(_file);
        _contentsString = Utils.readContentsAsString(_file);
        _blobId = Utils.sha1('b' + _fileName + _contentsString);
    }

    /** Returns the name of the file. */
    public String getFileName() {
        return _fileName;
    }

    /** Returns the file object. */
    public File getFile() {
        return _file;
    }

    /** Returns the contents of the file. */
    public byte[] getFileContents() {
        return _contents;
    }

    /** Returns the contents of the file as a string. */
    public String getFileContentsString() {
        return _contentsString;
    }

    /** Returns the ID of the blob. */
    public String getBlobId() {
        return _blobId;
    }

    /** Byte[] contents of a file.*/
    private byte[] _contents;

    /** String name of a file.*/
    private String _fileName;

    /** String ID of a blob.*/
    private String _blobId;

    /** String contents of a file.*/
    private String _contentsString;

    /** A file.*/
    private File _file;

}
