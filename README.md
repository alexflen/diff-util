# The course of the basics of programming on the MCS of St. Petersburg State University
## Project 1: diff utility

[Problem statement](./TASK.md )

### Using the utility
The command is entered in one of two ways: via the console or via the compilation options.
The command has the following form:
- file_name 1 file_name 2 [options]

#### What does the team do
The program compares two files with the names (paths) file1 and file2 in the style of the diff utility
and outputs the difference between them, taking into account the passed options. File names and paths to them should not contain
forbidden and whitespace characters.

#### Options
The options come after the file names and should start with -. The options parameters come after the option declaration and the sign =
- -i -- ignore empty lines. Files will be compared without taking into account empty lines.
The option has no parameters
- -w (-W) -- output not to the console, but to a file. w overwrites the contents of the file, and W overwrites to the file.
The output file name is passed as a parameter in the following format -w=output file name.
- -s -- string display parameters. Parameters: u (show unchanged rows), d (show deleted rows),
a (show added rows). Parameters are passed after the equal sign, if you need to specify several parameters,
they are specified without spaces in a row. For example, -s=au will show the unchanged and added rows.
- -h -- parameters for hiding additional information. Parameters: n (hide line numbers), i (hide information about
the quantities of each type of lines), s (hide the status output (deleted, added, not changed) for each output line).
Parameters are passed in the same way as in -s.

If several identical options are set in the command, only the last one is taken into account.

#### Entering a command
You just need to enter the command in one of the ways with all the options you want
to apply. If something is wrong (for example, a file name or an option), the program will tell
you what error occurred. Example command:
- a.txt b.txt -i -s=ua -h=i -W=answ.txt . Here the file comparison will take place a.txt and b.txt
ignoring empty lines, only unchanged and added lines will be shown
, information about the number of lines will not be shown, the command output will be written to a file. answ.txt

If you call a command without parameters (or some parameter), the default values will be applied
(but the names of the two files are required for comparison).

By default:
- Empty lines are not ignored
- All lines are displayed: unchanged, deleted and added
- All information is displayed: line numbers, line status and quantity information
- The result is output to the console

#### Program output
Example of program output:

[1] + It is added 1

(1)[2] | Hello

[3] + It is added 2

[4] + This is

(2)[5] | This is a file

(3) - It is deleted

[6] + No no

(4)[7] | It is unchanged

Added rows 4, deleted rows 1, not changed 3

(there may be whitespace characters at the beginning of each line, but they are not displayed
when viewing README.md )

Here the line numbers in the first file are given in parentheses, and in the second file - in square.
If there is no line in the file, the appropriate number of spaces is output instead for smooth output.
Next, each line has its status -- + means an added line, - means a deleted line, | means a line
that remains unchanged. Next comes the line itself. At the end of the output, information is output:
for each row status, the number of such rows is displayed.

### Implementation
The program code is divided into 4 files -- main.kt, supporting.kt, doInputOutput.kt, workWithInputOutputFormat.kt.
In main.kt contains the main program code: findLCS functions, outputAnswer, main and date classes.
In supporting.kt contains auxiliary functions: outputError, checkIfOkFormatParams, numberLength.
doInputOutput contains functions that perform input and output and their easy processing: parseInput, writeAnywhere, readFiles.
workWithInputOutputFormat contains functions that handle input and output: outputLine and processCommand

The utility was implemented using a search algorithm
The Largest Common Subsequence. The following functions have been implemented:
- outputError -- error output function to the console
- checkIfOkFormatParams -- function for checking compliance with the format of calling an option with the parameter
- parseInput -- the function of splitting the input string (from the console) by spaces
- numberLength -- the function returns the length of an integer
- writeAnywhere -- the function of writing a given string to a file or not (depending on the parameters)
- outputLine -- single line processing and output function (using writeAnywhere)
- outputAnswer -- function for processing the NOP search result and output of response strings
(using the outputLine function). The lines are output in the order in which they
are determined by the pointer method: pointers to lines added, deleted are supported,
while we go with the main pointer on unchanged lines
- findLCS -- NOP search function by lines of two files (uses two-dimensional dynamics). Strings are compared
using hashes to speed up work on large files (with long strings)
- readFiles -- a function that reads two files and writes their contents to a pair of lists
- processCommand -- a function that processes a command: it understands what the options do
and passes the received parameters to
the outputAnswer - main function -- starts the algorithm

And the following classes were implemented:
- class LineNumbers -- in fact, a pair of two numbers: the line number in the first file and the line number
in the second file; needed to return the findLCS function
- class LineInfo -- essentially three lists: added, deleted and unchanged rows,
information about rows is stored using LineNumbers; needed to return the findLCS function
- class FileLines -- list of lines for each file

### Testing
There are two tester files for testing the project:
- Test1.kt -- tests individual functions
- Test2.kt -- tests the entire program

Tests for Test2.kt are stored in the tests folder, the
temp folder is used during testing. The test folder stores the test files and the answers to them. To get large tests
(tests 3 and 4, 9), you need to run the program (generate_files.py ), which generates them,
the generated tests can be cleaned using the program clean_generated.py .
The large generated file weighs about 55 MB (test 4). Another large generated file
contains about 5000 lines (test 9).
