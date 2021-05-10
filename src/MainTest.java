package edu.gatech.seclass.cleave;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

// DO NOT ALTER THIS CLASS. Use it as an example for MyMainTest.java

public class MainTest {

    private ByteArrayOutputStream outStream;
    private ByteArrayOutputStream errStream;
    private PrintStream outOrig;
    private PrintStream errOrig;
    private Charset charset = StandardCharsets.UTF_8;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        outStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outStream);
        errStream = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(errStream);
        outOrig = System.out;
        errOrig = System.err;
        System.setOut(out);
        System.setErr(err);
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(outOrig);
        System.setErr(errOrig);
    }

    /*
     *  TEST UTILITIES
     */

    // Create File Utility
    private File createTmpFile() throws Exception {
        File tmpfile = temporaryFolder.newFile();
        tmpfile.deleteOnExit();
        return tmpfile;
    }

    // Write File Utility
    private File createInputFile(String input) throws Exception {
        File file = createTmpFile();

        OutputStreamWriter fileWriter =
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);

        fileWriter.write(input);

        fileWriter.close();
        return file;
    }

    /*
     *   TEST CASES
     */

    // Frame #: Instructor test example
    @Test
    public void mainTest1() throws Exception {
        File inputFile = createInputFile("0123456789" + System.lineSeparator() + "abcdefghi");

        String args[] = {"-c", "2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("The files differ!", "1" + System.lineSeparator() + "b" + System.lineSeparator(),
                afterFile);
    }

    // Frame #: Instructor test example
    @Test
    public void mainTest2() throws Exception {
        File inputFile = createInputFile("0123456789");

        String args[] = {"-c", "15", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("The files differ!", System.lineSeparator(), afterFile);
    }

    // Frame #: Instructor test example
    @Test
    public void mainTest3() throws Exception {
        File inputFile = createInputFile("0123456789");

        String args[] = {"-c", "5,1", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("The files differ!", "04" + System.lineSeparator(), afterFile);
    }

    // Frame #: Instructor test example
    @Test
    public void mainTest4() throws Exception {
        File inputFile = createInputFile("0123456789");

        String args[] = {"-c", "-5", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("The files differ!", "01234" + System.lineSeparator(), afterFile);
    }

    // Frame #: Instructor test example
    @Test
    public void mainTest5() throws Exception {
        File inputFile = createInputFile("0123456789");

        String args[] = {"-c", "4-7", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("The files differ!", "3456" + System.lineSeparator(), afterFile);
    }

    // Frame #: Instructor test example
    @Test
    public void mainTest6() throws Exception {
        File inputFile = createInputFile("012:34:5678:9");

        String args[] = {"-d", ":", "-f", "2,4", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("The files differ!", "34:9" + System.lineSeparator(), afterFile);
    }

    // Frame #: Instructor test example
    @Test
    public void mainTest7() throws Exception {
        //no arguments on the command line will pass an array of length 0 to the application (not a null).
        String args[] = new String[0];
        Main.main(args);
        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

}

