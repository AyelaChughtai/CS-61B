## Blob:
A blob object containing contents of a file.

## Commit:
A commit object containing blobs staged for addition or removal.

## Commit Tree:
A commit tree object containing commit and branch information.

## Staging Area:
A staging area object where blobs are staged for addition or removal.

## Main:
Processes commands entered to create and run the .gitlet system. Outputs error message on incorrect inputs. Implements persistence for the system.

## Testing:

    Makefile            Directions for testing.

    *-1.in
    *-2.in	            Test cases.  Each one is input to a testing script.

    *-1.std
    *-2.std		          Correct output from the corresponding .in files,
                        containing log, global log, status, and error messages.
