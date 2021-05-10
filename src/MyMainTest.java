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

public class MyMainTest {
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
        errOrig = System.out;
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
     *   EXTRA METHODS
     */

    // random string generator with indicated string length. Test Max length of the string.
    public static class RandomString {
        public static String getAlphaNumericString(int n) {
            String AlphaNumericString =
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                            + "0123456789"
                            + "abcdefghijklmnopqrstuvxyz";
            StringBuilder sb = new StringBuilder(n);
            for (int i = 0; i < n; i++) {
                int index
                        = (int) (AlphaNumericString.length()
                        * Math.random());
                sb.append(AlphaNumericString
                        .charAt(index));
            }
            return sb.toString();
        }
    }

    // string with specific length：maxi length
    public static class StringWithSpecifiedCharsMaxilength {
        static final String strCharacter = "a";
        static final int stringLength = 1000;

        public static void main(String[] args) {
            System.out.println(createString());
        }

        public static String createString() {
            StringBuilder sbString =
                    new StringBuilder(stringLength);
            sbString.append(strCharacter.repeat(stringLength));
            return sbString.toString();
        }
    }

    // string with specific length: 0 length
    public static class StringWithSpecifiedCharsZerolength {
        static final String strCharacter = "a";
        static final int stringLength = 0;

        public static void main(String[] args) {
            System.out.println(createString());
        }

        public static String createString() {
            StringBuilder sbString =
                    new StringBuilder(stringLength);
            sbString.append(strCharacter.repeat(stringLength));
            return sbString.toString();
        }
    }

    /*
     *   TEST CASES
     */

    // Frame #: 1
    @Test
    public void cleaveTest1() throws Exception {
        File inputFile = createInputFile(StringWithSpecifiedCharsZerolength.createString());

        String args[] = {"-c", "2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("" + System.lineSeparator(), afterFile);
    }

    // Frame #: 2
    @Test
    public void cleaveTest2() throws Exception {
        File inputFile = createInputFile(StringWithSpecifiedCharsMaxilength.createString());

        String args[] = {"-c", "2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("a" + System.lineSeparator(), afterFile);
    }

    // Frame #: 3
    @Test
    public void cleaveTest3() throws Exception {
        File inputFile = createInputFile("0123456" + System.lineSeparator());

        String args[] = {"-c", "0", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #: 4
    /*@Test
    public void cleaveTest4() throws Exception {
        File inputFile = createInputFile("123456");

        String args[] = {"-c", "-5", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("12345" + System.lineSeparator(), afterFile);
    }*/

    // Frame #: 5
    @Test
    public void cleaveTest5() throws Exception {
        String maximumInt = Integer.toString(Integer.MAX_VALUE); // 2147483647
        File inputFile = createInputFile(maximumInt + System.lineSeparator());

        String args[] = {"-c", "2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("1" + System.lineSeparator(), afterFile);
    }

    // Frame #: 6
    @Test
    public void cleaveTest6() throws Exception {
        String minimumInt = Integer.toString(Integer.MIN_VALUE);
        File inputFile = createInputFile(minimumInt + System.lineSeparator());

        String args[] = {"-c", "2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("2" + System.lineSeparator(), afterFile);
    }

    // Frame #:7
    @Test
    public void cleaveTest7() throws Exception {
        String str = "a";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "1", "3-4", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:8
    @Test
    public void cleaveTest8() throws Exception {
        String str = "a";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "1", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("a" + System.lineSeparator(), afterFile);
    }

    // Frame #:9
    @Test
    public void cleaveTest9() throws Exception {
        String str = "a";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "1, 2", "3-4", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:10
    @Test
    public void cleaveTest10() throws Exception {
        String str = "a";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "1, 2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:11
    @Test
    public void cleaveTest11() throws Exception {
        String str = "a";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "1-2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("a" + System.lineSeparator(), afterFile);
    }

    // Frame #:12
    @Test
    public void cleaveTest12() throws Exception {
        String str = "a";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:13
    @Test
    public void cleaveTest13() throws Exception {
        String str = "a";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "10", "12-15", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:14
    @Test
    public void cleaveTest14() throws Exception {
        String str = "a";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "10", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals(System.lineSeparator(), afterFile);
    }

    // Frame #:15
    @Test
    public void cleaveTest15() throws Exception {
        String str = "a";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "10, 25", "30-32", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:16
    @Test
    public void cleaveTest16() throws Exception {
        String str = "a";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "10, 25", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:17
    @Test
    public void cleaveTest17() throws Exception {
        String str = "a";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "10-25", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals(System.lineSeparator(), afterFile);
    }

    // Frame #:18
    @Test
    public void cleaveTest18() throws Exception {
        String str = "a";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:19
    @Test
    public void cleaveTest19() throws Exception {
        String str = "a ";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "1", "2-3", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:20
    @Test
    public void cleaveTest20() throws Exception {
        String str = "a ";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "1", "3-4", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:21
    @Test
    public void cleaveTest21() throws Exception {
        String str = "a ";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "1,2", "3-4", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:22
    @Test
    public void cleaveTest22() throws Exception {
        String str = "a ";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "1,2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("a " + System.lineSeparator(), afterFile);
    }

    // Frame #:23
    @Test
    public void cleaveTest23() throws Exception {
        String str = "a ";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "1,2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("a " + System.lineSeparator(), afterFile);
    }

    // Frame #:24
    @Test
    public void cleaveTest24() throws Exception {
        String str = "a ";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:25
    @Test
    public void cleaveTest25() throws Exception {
        String str = "a ";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "9", "10-25", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:26
    @Test
    public void cleaveTest26() throws Exception {
        String str = "a ";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "9", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals(System.lineSeparator(), afterFile);
    }

    // Frame #:27
    @Test
    public void cleaveTest27() throws Exception {
        String str = "a ";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "9,10", "25-27", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:28
    @Test
    public void cleaveTest28() throws Exception {
        String str = "a ";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "9,10", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals(System.lineSeparator(), afterFile);
    }

    // Frame #:29
    @Test
    public void cleaveTest29() throws Exception {
        String str = "a ";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "9-10", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals(System.lineSeparator(), afterFile);
    }

    // Frame #:30
    @Test
    public void cleaveTest30() throws Exception {
        String str = "a ";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:31
    @Test
    public void cleaveTest31() throws Exception {
        String str = "abcdefghijkl";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "1", "3-5", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:32
    @Test
    public void cleaveTest32() throws Exception {
        String str = "abcdefghijkl";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("b" + System.lineSeparator(), afterFile);
    }

    // Frame #:33
    @Test
    public void cleaveTest33() throws Exception {
        String str = "abcdefghijkl";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "1,2", "3-5", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:34
    @Test
    public void cleaveTest34() throws Exception {
        String str = "abcdefghijkl";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "1,2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("ab" + System.lineSeparator(), afterFile);
    }

    // Frame #:35
    @Test
    public void cleaveTest35() throws Exception {
        String str = "abcdefghijkl";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "-5", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("abcde" + System.lineSeparator(), afterFile);
    }

    // Frame #:36
    @Test
    public void cleaveTest36() throws Exception {
        String str = "abcdef";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:37
    @Test
    public void cleaveTest37() throws Exception {
        String str = "abcdefghi";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "100", "300-500", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    //Frame #:38
    @Test
    public void cleaveTest38() throws Exception {
        String str = "abcdefghi";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "100", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals(System.lineSeparator(), afterFile);
    }

    // Frame #:39
    @Test
    public void cleaveTest39() throws Exception {
        String str = "abcdefghi";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "100, 200", "300-350", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:40
    @Test
    public void cleaveTest40() throws Exception {
        String str = "abcdefghi";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "100, 200", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:41
    @Test
    public void cleaveTest41() throws Exception {
        String str = "abcdefghi";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "100-200", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals(System.lineSeparator(), afterFile);
    }

    // Frame #:42
    @Test
    public void cleaveTest42() throws Exception {
        String str = "abcdef";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:43
    @Test
    public void cleaveTest43() throws Exception {
        String str = "a:b:c:d:e:f:g:h:i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "1", "3-5", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:44
    @Test
    public void cleaveTest44() throws Exception {
        String str = "a:b:c:d:e:f:g:h:i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "1-", "-d", ":", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("a:b:c:d:e:f:g:h:i" + System.lineSeparator(), afterFile);
    }

    // Frame #:45
    @Test
    public void cleaveTest45() throws Exception {
        String str = "a:b:c:d:e:f:g:h:i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "1,3", "5-7", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:46
    @Test
    public void cleaveTest46() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "1,3", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("a,c" + System.lineSeparator(), afterFile);
    }

    // Frame #:47
    @Test
    public void cleaveTest47() throws Exception {
        String str = "a:b:c:d:e:f:g:h:i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "1-3", "-d", ":", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("a:b:c" + System.lineSeparator(), afterFile);
    }

    // Frame #:48
    @Test
    public void cleaveTest48() throws Exception {
        String str = "a:b:c:d:e:f";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:49
    @Test
    public void cleaveTest49() throws Exception {
        String str = "a:b:c:d:e:f:g:h:i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "95", "100-200", "-d :", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:50
    @Test
    public void cleaveTest50() throws Exception {
        String str = "a:b:c:d:e:f:g:h:i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "95", "d :", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:51
    @Test
    public void cleaveTest51() throws Exception {
        String str = "a:b:c:d:e:f:g:h:i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "95, 100", "500-550", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:52
    @Test
    public void cleaveTest52() throws Exception {
        String str = "a:b:c:d:e:f:g:h:i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "95, 100", "d :", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:53
    @Test
    public void cleaveTest53() throws Exception {
        String str = "a:b:c:d:e:f:g:h:i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "95-100", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals(System.lineSeparator(), afterFile);
    }

    // Frame #:54
    @Test
    public void cleaveTest54() throws Exception {
        String str = "a:b:c:d:e:f";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:55
    //cleave -c 2 FILE
    //input FILE:
    //  0123456789↵
    //  abcdefghi↵
    //stdout:
    //  1↵
    //  b↵
    @Test
    public void cleaveTest55() throws Exception {
        String str1 = "0123456789";
        String str2 = "abcdefghi";
        File inputFile = createInputFile(str1 + System.lineSeparator() + str2 + System.lineSeparator());

        String args[] = {"-c", "2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("1" + System.lineSeparator() + "b" + System.lineSeparator(), afterFile);
    }

    // Frame #:56
    //Example 5:
    //cleave -c 5,1 FILE
    //input FILE:
    //  0123456789↵
    //stdout:
    //  04↵
    @Test
    public void cleaveTest56() throws Exception {
        String str = "0123456789";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "5,1", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("04" + System.lineSeparator(), afterFile);
    }

    // Frame #:57
    @Test
    public void cleaveTest57() throws Exception {
        String str = "0123456789";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "4-7,2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("13456" + System.lineSeparator(), afterFile);
    }

    // Frame #:58
    // test max int 2147483647
    @Test
    public void cleaveTest58() throws Exception {
        String str = "0123456789";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "2147483647", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("" + System.lineSeparator(), afterFile);
    }

    // Frame #:59
    // test min int -2147483648
    @Test
    public void cleaveTest59() throws Exception {
        String str = "0123456789";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "-10000", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("0123456789" + System.lineSeparator(), afterFile);
    }

    // Frame #:60
    // test input = "";
    @Test
    public void cleaveTest60() throws Exception {
        String str = "";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("" + System.lineSeparator(), afterFile);
    }

    // Frame #:61
    // test -c 2-1
    @Test
    public void cleaveTest61() throws Exception {
        String str = "0123456789";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "2-1", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:62
    // test -c 2-2
    @Test
    public void cleaveTest62() throws Exception {
        String str = "0123456789";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "2-2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("1" + System.lineSeparator(), afterFile);
    }

    // from-to, single
    // Frame #:63
    // test -c 1-2, bigger than size
    @Test
    public void cleaveTest63() throws Exception {
        String str = "0123456789";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "1-2,100", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("01" + System.lineSeparator(), afterFile);
    }

    // Frame #:64
    // test -c 2-1, bigger than size
    @Test
    public void cleaveTest64() throws Exception {
        String str = "0123456789";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "2-1, 100", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:65
    // test -c 2-2, bigger than size
    @Test
    public void cleaveTest65() throws Exception {
        String str = "0123456789";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "2-2,100", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("1" + System.lineSeparator(), afterFile);
    }

    // Frame #:66
    // test -c 1-2, bigger than size, bigger than size
    @Test
    public void cleaveTest66() throws Exception {
        String str = "0123456789";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "1-2,100,100", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("01" + System.lineSeparator(), afterFile);
    }

    // Frame #:67
    // test -c 2-1, 2-1
    @Test
    public void cleaveTest67() throws Exception {
        String str = "0123456789";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "2-1, 2-1", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:68
    // test -c 2-2, 2-2
    @Test
    public void cleaveTest68() throws Exception {
        String str = "0123456789";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "2-1, 2-2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", afterFile);
    }

    // Frame #:69
    // test -c bigger than size-
    @Test
    public void cleaveTest69() throws Exception {
        String str = "0123456789";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "100-", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("" + System.lineSeparator(), afterFile);
    }

    // Frame #:70
    // test -f and -c same time show up
    @Test
    public void cleaveTest70() throws Exception {
        String str = "0123456789";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "1", "-f", "2-4", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:71
    // test -f without -d but use "," as delimer
    @Test
    public void cleaveTest71() throws Exception {
        String str = "0,1,2,3,4,5";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2-4", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("0,1,2,3,4,5" + System.lineSeparator(), afterFile);
    }

    // Frame #:72
    // test -f without -d but all tabs without alphanumerics
    @Test
    public void cleaveTest72() throws Exception {
        String str = "\t\t\t";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "1", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("" + System.lineSeparator(), afterFile);
    }

    // Frame #:73
    // test -f, -d with two delimers: / and :
    @Test
    public void cleaveTest73() throws Exception {
        String str = "0,1,2:3:4,5";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2-4", "-d", ", :", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:74
    // test -f, -d but delimer in the first position
    /*@Test
    public void cleaveTest74() throws Exception {
        String str = ",1,2,3,4,5";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "1-4", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("1,2,3" + System.lineSeparator(), afterFile);
    }*/

    // Frame #:75
    // test -f, -d but delimer in the last position
    @Test
    public void cleaveTest75() throws Exception {
        String str = "1,2,3,4,5,";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2-5", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("2,3,4,5" + System.lineSeparator(), afterFile);
    }

    // Frame #:76
    // test -f, -d but two consecutive delimmers: ,,
    /*@Test
    public void cleaveTest76() throws Exception {
        String str = "1,2,3,,,4,5";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2-5", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("2,3" + System.lineSeparator(), afterFile);
    }*/

    // Frame #:77
    // test -f, -d but all alphanumerics no delimers
    @Test
    public void cleaveTest77() throws Exception {
        String str = "12345";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2-5", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("12345" + System.lineSeparator(), afterFile);
    }

    // Frame #:78
    // test all tabs but invoke -d with ,
    @Test
    public void cleaveTest78() throws Exception {
        String str = "1\t2\t3\t4\t5";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2-5", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("1\t2\t3\t4\t5" + System.lineSeparator(), afterFile);
    }

    // Frame #:79
    // input is null not ""
    @Test
    public void cleaveTest79() throws Exception {
        String str = null;
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2-5", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals(null + System.lineSeparator(), afterFile);
    }

    // Frame #:80
    // input is null not ""
    @Test
    public void cleaveTest80() throws Exception {
        String str = "";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "2-5", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", outStream.toString().trim());
    }

    // Frame #:81
    // use -c but added -d
    @Test
    public void cleaveTest81() throws Exception {
        String str = "abcdefghi";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "2-5", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:82
    // use -f but 5-2
    @Test
    public void cleaveTest82() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "5-2", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:83
    // use -f but with maxi length of input
    @Test
    public void cleaveTest83() throws Exception {
        File inputFile = createInputFile(StringWithSpecifiedCharsMaxilength.createString());

        String args[] = {"-f", "5-2", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:84
    // use -f but zero length of input
    @Test
    public void cleaveTest84() throws Exception {
        String str = "";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "5-2", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:85
    // use -f with from-to and single
    @Test
    public void cleaveTest85() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "5-2, 6", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:86
    // N = 0, with -c and -f
    @Test
    public void cleaveTest86() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "0", "-f", "0", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:87
    // N = M, with -c and -f
    @Test
    public void cleaveTest87() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "5-5", "-f", "5-5, 6", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", outStream.toString().trim());
    }

    // Frame #:88
    // one -f with many -d
    @Test
    public void cleaveTest88() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2-5, 6", "-d", ",:", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", outStream.toString().trim());
    }

    // Frame #:89
    // string = 1:2:3, -d, ,
    @Test
    public void cleaveTest89() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2-5, 6", "-d", ":", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", outStream.toString().trim());
    }

    // Frame #:90
    // input without line separator
    @Test
    public void cleaveTest90() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str);

        String args[] = {"-f", "2-5, 6", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("", outStream.toString().trim());
    }

    // Frame #:91
    // -d is infront of the range
    // -d : -f 2-4
    @Test
    public void cleaveTest91() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str);

        String args[] = {"-d", ",", "-f", "2-5,6", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("b,c,d,e,f" + System.lineSeparator(), afterFile);
    }

    // 87-91 there should be an error.
    // Frame #:92
    // multiple -c
    @Test
    public void cleaveTest92() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "-c", "0", "-f", "0", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:93
    // multiple -f
    @Test
    public void cleaveTest93() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "0", "-f", "-f", "0", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:94
    // -c is ""
    @Test
    public void cleaveTest94() throws Exception {
        String str = "abcdefgfi";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:95
    // -f is ""
    @Test
    public void cleaveTest95() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:96
    // -c is aaa
    @Test
    public void cleaveTest96() throws Exception {
        String str = "abcdefghi";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "aaa", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:97
    // should be right, test -f function
    @Test
    public void cleaveTest97() throws Exception {
        String str = "a\tb\tc\td\te\tf\tg\th\ti";
        File inputFile = createInputFile(str);

        String args[] = {"-f", "2-5,6", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("b\tc\td\te\tf" + System.lineSeparator(), afterFile);
    }

    // Frame #:98
    //
    @Test
    public void cleaveTest98() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "1", "-c", "1", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:99
    @Test
    public void cleaveTest99() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "1", "-c", "1", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:100
    // Range is "2, "
    @Test
    public void cleaveTest100() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2, ", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:101
    // option is ""
    @Test
    public void cleaveTest101() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "", "-d", "", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:102
    @Test
    public void cleaveTest102() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "", "-d", "!,#,", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:103
    // multiple -d
    @Test
    public void cleaveTest103() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "", "-d", "!,#,", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:104
    @Test
    public void cleaveTest104() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2-5", "-d", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:105
    @Test
    public void cleaveTest105() throws Exception {
        String str = "abcdefghi";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "-5-7", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:106
    @Test
    public void cleaveTest106() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "-5-7", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:107
    @Test
    public void cleaveTest107() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2-5", "-d", "-5-7", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:108
    @Test
    public void cleaveTest108() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2-5 ", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:109
    @Test
    public void cleaveTest109() throws Exception {
        String str = "abcdefghi";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "2-5 ", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:110
    @Test
    public void cleaveTest110() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2-5", "-d", "5-7 ", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:111
    @Test
    public void cleaveTest111() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "aaa", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:112
    @Test
    public void cleaveTest112() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2-5", "-d", "aaa", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:113
    @Test
    public void cleaveTest113() throws Exception {
        String str = "abcdefghi";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "2-2", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("b" + System.lineSeparator(), afterFile);
    }

    // Frame #:114
    @Test
    public void cleaveTest114() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2-2", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = outStream.toString();

        assertEquals("b" + System.lineSeparator(), afterFile);
    }

    // Frame #:115
    @Test
    public void cleaveTest115() throws Exception {
        String str = "abcdefghi";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", "5-2", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:116
    @Test
    public void cleaveTest116() throws Exception {
        String str = "a,b,c,d,e,f,g,h,i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "5-2", "-d", ",", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:117
    @Test
    public void cleaveTest117() throws Exception {
        //no arguments on the command line will pass an array of length 0 to the application (not a null).
        String args[] = new String[0];
        Main.main(args);
        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:118
    @Test
    public void cleaveTest118() throws Exception {
        String str = "abcdefghi";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:119
    @Test
    public void cleaveTest119() throws Exception {
        String str = "abcdefghi";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-c", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:120
    @Test
    public void cleaveTest120() throws Exception {
        String str = "a\tb\tc\td\te\tf\tg\th\ti";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:121
    @Test
    public void cleaveTest121() throws Exception {
        String str = "a:b:c:d:e:f:g:h:i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "d", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }

    // Frame #:122
    @Test
    public void cleaveTest122() throws Exception {
        String str = "a:b:c:d:e:f:g:h:i";
        File inputFile = createInputFile(str + System.lineSeparator());

        String args[] = {"-f", "2-5", "d", inputFile.getPath()};
        Main.main(args);

        var afterFile = errStream.toString();

        assertEquals("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>", errStream.toString().trim());
    }
}

