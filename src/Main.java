package edu.gatech.seclass.cleave;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            usage();
            return;
        }
        String filename = args[args.length - 1];
        File file = new File(filename);
        if (!file.exists()) {
            System.err.println("Failed to open input file");
            return;
        }
        Scanner scanner = new Scanner(file);
        List<String> data = new ArrayList<>();
        while (scanner.hasNextLine()) {
            data.add(scanner.nextLine());
        }
        String cmd = args[0];
        if (cmd.equals("-c")) {
            Arrays.sort(args, 1, args.length - 1);
            dealC(args, data);
        } else if (cmd.equals("-d") || cmd.equals("-f")) {
            dealF(args, data);
        } else {
            usage();
        }
    }

    private static void dealF(String[] args, List<String> data) {
        // -f xxx filename
        // -f xxx -d filename
        /// -f xxx -d x filename
        if (args.length != 3 && args.length != 4 && args.length != 5) {
            usage();
            return;
        }
        if (data.isEmpty()) {
            System.out.println();
            return;
        }
        String cmd = args[0];
        boolean hasDeli = false;
        String deliChar = "\t";
        int fStart = 1;
        if (cmd.equals("-f")) {
            int argEnd = args.length - 2;
            for (int i = args.length - 2; i > 0; i--) {
                if (args[i].equals("-d") && !hasDeli) {
                    if (args[i + 1].length() != 1) {
                        usage();
                        return;
                    } else {
                        deliChar = args[i + 1];
                    }
                    argEnd = i - 1;
                    hasDeli = true;
                }
            }
            // Check whether there are illegal characters in the interval
            if (argEnd != 1) {
                usage();
                return;
            }
            if (argEnd == args.length - 2 && hasDeli) {
                for (String arg : args) {
                    if (arg.isEmpty()) {
                        usage();
                        return;
                    }
                }
                return;
            }
        } else {

            if (args[1].length() != 1 && !args[1].equals("-f")) {
                usage();
                return;
            }// Delimiter exists -d x -f xxx filename
            if (args[1].length() == 1) {
                deliChar = args[1];
                fStart = 3;
            } else {
                // No separator
                // -d -f xxx filename
                fStart = 2;
                if (args.length != 4) {
                    usage();
                    return;
                }
            }
        }
        boolean isSpecial = false;
        if (isSpecialChar(deliChar)) {
            deliChar = "\\" + deliChar;
            isSpecial = true;
        }
        List<List<String>> res = new ArrayList<>();
        for (String datum : data) {
            List<String> row = new ArrayList<>();
            String[] split = datum.split(deliChar);
            String arg = args[fStart];
            int dic = getDig(arg);

            if (dic != Integer.MIN_VALUE && !arg.startsWith("-")) {
                if (dic == 0) {
                    usage();
                    return;
                }
                if (split.length >= dic) {
                    row.add(split[dic - 1]);
                } else {
                    row.add("");
                }
            } else if (arg.contains(",")) {
                String[] dics = arg.split(",");
                Arrays.sort(dics);
                int[] index = new int[dics.length];
                for (int m = 0; m < dics.length; m++) {
                    if (dics[m].contains("-")) {
                        switch (dealExtra(split, row, dics[m])) {
                            case 1:
                                usage();
                                return;
                            case 2:
                                row.add("");
                        }
                        continue;
                    }
                    index[m] = getDig(dics[m]);
                    if (index[m] == Integer.MIN_VALUE) {
                        usage();
                        return;
                    }
                    if (index[m] <= split.length) {
                        row.add(split[index[m] - 1]);
                    }
                }
            } else if (arg.startsWith("-")) {
                dic = getDig(arg.substring(1));
                if (dic == Integer.MIN_VALUE) {
                    usage();
                    return;
                }
                row.addAll(Arrays.asList(split).subList(0, Math.min(dic, split.length)));
            } else if (arg.endsWith("-")) {
                dic = getDig(arg.substring(0, arg.length() - 1));
                if (dic == Integer.MIN_VALUE) {
                    row.add("");
                }
                if (dic > split.length) {
                    row.add("");
                } else
                    row.addAll(Arrays.asList(split).subList(dic - 1, split.length));

            } else if (arg.contains("-")) {
                switch (dealExtra(split, row, arg)) {
                    case 1:
                        usage();
                        break;
                    case 2:
                        row.add(datum);
                        break;
                    default:
                }

            } else {
                usage();
                return;
            }
            res.add(row);
        }
        if (isSpecial) {
            deliChar = deliChar.substring(1);
        }

        List<List<String>> temp = new ArrayList<>();
        for (List<String> re : res) {
            List<String> row = new ArrayList<>();
            for (String s : re) {
                if (!row.contains(s)) {
                    row.add(s);
                }
            }
            temp.add(row);
        }
        res = temp;
        for (List<String> re : res) {
            System.out.println(String.join(deliChar, re));
        }

    }

    private static boolean isSpecialChar(String del) {
        switch (del) {
            case "|":
                return true;
        }
        return false;
    }

    private static void dealC(String[] args, List<String> data) {
        // -c xxx filename
        if (args.length != 3) {
            usage();
            return;
        }
        if (data.isEmpty()) {
            System.out.println();
            return;
        }
        List<List<String>> res = new ArrayList<>();

        for (String d : data) {
            List<String> row = new ArrayList<>();

            String arg = args[1];
            int dic = getDig(arg);
            if (dic != Integer.MIN_VALUE && !arg.startsWith("-")) {
                if (dic == 0) {
                    usage();
                    return;
                }
                if (d.length() >= dic) {
                    row.add(d.charAt(dic - 1) + "");
                } else {
                    row.add("");
                }
            } else if (arg.contains(",")) {
                String[] dics = arg.split(",");
                Arrays.sort(dics);
                for (String s : dics) {
                    if (s.contains("-")) {
                        if (dealExtra(d, row, s))
                            return;
                        continue;
                    }
                    int index = getDig(s);
                    if (index == Integer.MIN_VALUE) {
                        return;
                    }
                    if (index <= d.length()) {
                        row.add(d.charAt(index - 1) + "");
                    }
                }

            } else if (arg.startsWith("-")) {
                dic = getDig(arg.substring(1));
                if (dic == Integer.MIN_VALUE) {
                    usage();
                    return;
                }

                row.add(d.substring(0, Math.min(dic, d.length())));
            } else if (arg.endsWith("-")) {
                dic = getDig(arg.substring(0, arg.length() - 1));
                if (dic == Integer.MIN_VALUE) {
                    row.add("");
                    row.add("");

                } else if (dic > d.length()) {
                    row.add("");
                } else
                    row.add(d.substring(dic));
            } else if (arg.contains("-")) {
                if (dealExtra(d, row, arg)) {
                    usage();
                    return;
                }

            } else {
                usage();
                return;
            }
            res.add(row);

        }
        List<List<String>> temp = new ArrayList<>();
        for (List<String> re : res) {
            List<String> row = new ArrayList<>();
            for (String s : re) {
                if (!row.contains(s)) {
                    row.add(s);
                }
            }
            temp.add(row);
        }
        res = temp;

        for (List<String> re : res) {
            System.out.println(String.join("", re));
        }
    }

    private static int dealExtra(String[] d, List<String> row, String arg) {
        String[] nums = arg.split("-");
        int start = getDig(nums[0]);
        int end = getDig(nums[1]);
        if (start > end || start == Integer.MIN_VALUE) {
            return 1;
        }
        if (start > d.length) {
            return 2;
        }
        row.addAll(Arrays.asList(d).subList(start - 1, Math.min(end, d.length)));

        return 3;
    }

    private static boolean dealExtra(String d, List<String> row, String arg) {
        String[] nums = arg.split("-");
        int start = getDig(nums[0]);
        int end = getDig(nums[1]);
        if (start > end || start == Integer.MIN_VALUE) {
            return true;
        }
        if (start > d.length()) {
            System.out.println();
            return true;
        }
        row.add(d.substring(start - 1, Math.min(end, d.length())));
        return false;
    }

    private static int getDig(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }

    private static void usage() {
        System.err.println("Usage: cleave [-c <list> | -f <list> [-d <delim>]] <filename>");
    }

}

