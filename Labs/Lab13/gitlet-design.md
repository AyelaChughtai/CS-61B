# Gitlet Design Document

    Kathleen Kong & Ayela Chughtai


# Classes and Data Structures

**Blobs**
This class stores the contents of files and implements serializable. 

Fields:

1. byte[] contents (serialization thing)
2. String fileName
3. String blobId
    1. using cryptographic hash function, SHA-1
    2. include the word ‘b’ at the end (hashed)
4. String contents

**Commits** 
This class stores combinations of log messages, other metadata (time stamp, log message), a reference to a tree, and references to parent commits. 

Fields:

1. String commitID
    1. using cryptographic hash function, SHA-1
    2. include the word ‘c’ at the end (hashed)
2. Time Stamp (if else)
3. Log Message
4. Pointer to parent
5. HashMap<String (fileName), Blob> blobs

**Commit Tree**
Contains all commits. Split points have 2 children, all other commit nodes have 1 child. Current branch is by default the ‘head’.

Fields 

1. Master Branch (Pointer)
2. Current Branch (Pointer)
3. Other Branch (Pointer)
4. String Other Branch Name

**Staging Area**

1. HashMap File Name to Blob
2. branches
3. Staged Files
4. Removed Files
5. Modifications Not Staged For Commit
6. Branch currentBranch
7. HashMap File Name to Blob
# Algorithms

**Blobs**

1. initBlob(fileName): The class constructor. 
    1. Argument: file name (string)
    2. Creates a blob id using hash function (differentiates by adding the word blob)
    3. Serializes the file contents

**Commits** 

1. initCommit(string logMessage, string dateTime, Branch branch, HashMap<> blobs)
    1. Creates a commit id using hash function (differentiates by adding the word commit)
    2. if (commit tree is empty) {DateTime = 00:00:00 UTC, Thursday, 1 January 1970} else {DateTime = DateTime())
    3. Reassign master pointer to most recent commit 
2. dateTime()
    1. returns String of date/time/day 

**Commit Tree**

1. initCommitTree ()
    1. TreeMap
2. initialBranch set to master
3. branch (updates the current branch/commit)

**Staging Area**

1. initStagingArea() - takes in to arguments and initialised all features to the default (none)
2. status function takes in no arguments and returns what branches exist, what files are staged for addition/removal, modifications not staged for commit, untracked files (sorted in lexicographical order)
3. add (blobs)
4. clear() - removes staged files for addition/removal


**Persistence**
In order to persist the settings of the version control system, we will need to save the state of the files after each call to commit. To do this,

1. Write the commit objects to disk. We can serialize them into bytes that we can eventually write to files on disk. This can be done with writeObject method from the Utils class. We will make sure that our commit class implements the Serializable interface.
2. Write all the blob objects to disk. We can serialize the Rotor objects and write them to files on disk (for example, “wug1.txt”, “wug2.txt”, etc.). This can be done with the writeObject method from the Utils class. We will make sure that our blob class implements the Serializable interface.

In order to retrieve our state, before executing any code, we need to search for the saved files in the working directory (folder in which our program exists) and load the objects that we saved in them. Since we set on a file naming convention (for example, “wug1.txt”, “wug2.txt”, etc.) our program always knows which files it should look for. We can use the readObject method from the Utils class to read the data of files as and deserialize the objects we previously wrote to these files.


