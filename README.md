# cleave 

### Summary
cleave is a simple command-line utility written in Java that allows for removing sections from each line of a file.
### Syntax
cleave *OPTIONS FILE*
### Description
Program cleave prints selected parts of the lines in FILE to stdout, where FILE is the name of the input file and OPTIONS are one or more of the following:
- **-c** *LIST*

    select only the characters at the indices specified by LIST 


- **-f** *LIST*

    select only the fields at indices specified by LIST; also print any line that contains no delimiter character (default=TAB)


- **-d** *DELIM*

    use character DELIM instead of TAB as field delimiter (used only with option -f)

Use one and only one of **-c** or **-f**. Each *LIST* consists of either one range or many ranges separated by commas. Selected input is written in the same order that it is read, and is written exactly once. Each range is one of:
- **N**

    Nth character or field, counted from 1.
- **N-**

    from Nth character or field to the end of the line.
- **N-M**

    from Nth to Mth (included) character or field. Where N≤M. 

- **-M**
    
    from first to Mth (included) character or field.


### Notes

If **-d** is specified, *DELIM* must consist of a single character. Selected fields are printed to stdout delimited by *DELIM* (**default=TAB**).  Delimiter options (**-d**) are only valid for field selection (**-f**), and they can appear in either order.  There will always be one output line for each input line.

### Example of Usage
In the following, “↵” represents a newline character, and “→” represents a TAB character.

**Example 1:** 

    cleave -c 2 FILE

input FILE:

    0123456789↵
    abcdefghi↵

stdout:
    
    1↵
    b↵
**Example 2:**

    cleave -c 15 FILE
input FILE:
    
    0123456789↵
stdout:

    ↵

