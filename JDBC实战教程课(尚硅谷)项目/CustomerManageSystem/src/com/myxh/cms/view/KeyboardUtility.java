package com.myxh.cms.view;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * @author MYXH
 * @date 2023/5/16
 */
public class KeyboardUtility
{
    private static final Scanner scanner = new Scanner(System.in);

    public static int readInt()
    {
        int n;

        while (true)
        {
            String str = readKeyBoard(8, false);

            try
            {
                n = Integer.parseInt(str);
                break;
            }
            catch (NumberFormatException e)
            {
                System.out.print("数字输入错误，请重新输入：");
            }
        }

        return n;
    }

    public static int readInt(int defaultValue)
    {
        int n;

        while (true)
        {
            String str = readKeyBoard(8, true);

            if (str.equals(""))
            {
                return defaultValue;
            }

            try
            {
                n = Integer.parseInt(str);
                break;
            }
            catch (NumberFormatException e)
            {
                System.out.print("数字输入错误，请重新输入：");
            }
        }

        return n;
    }

    public static BigDecimal readBigDecimal()
    {
        BigDecimal bigDecimal;

        while (true)
        {
            String str = readKeyBoard(8, false);

            try
            {
                bigDecimal = new BigDecimal(str);
                break;
            }
            catch (NumberFormatException e)
            {
                System.out.print("数字输入错误，请重新输入：");
            }
        }

        return bigDecimal;
    }

    public static BigDecimal readBigDecimal(BigDecimal defaultValue)
    {
        BigDecimal bigDecimal;

        while (true)
        {
            String str = readKeyBoard(8, true);

            if (str.equals(""))
            {
                return defaultValue;
            }

            try
            {
                bigDecimal = new BigDecimal(str);
                break;
            }
            catch (NumberFormatException e)
            {
                System.out.print("数字输入错误，请重新输入：");
            }
        }

        return bigDecimal;
    }

    public static char readChar()
    {
        String str = readKeyBoard(1, false);

        return str.charAt(0);
    }

    public static char readChar(char defaultValue)
    {
        String str = readKeyBoard(1, true);

        return (str.length() == 0) ? defaultValue : str.charAt(0);
    }

    public static String readString(int limit)
    {
        return readKeyBoard(limit, false);
    }

    public static String readString(int limit, String defaultValue)
    {
        String str = readKeyBoard(limit, true);

        return str.equals("")? defaultValue : str;
    }

    public static void readReturn()
    {
        System.out.print("按回车键继续...");
        readKeyBoard(100, true);
    }

    public static char readMenuSelection()
    {
        char c;

        while (true)
        {
            String str = readKeyBoard(1, false);

            c = str.charAt(0);

            if (c != '1' && c != '2' && c != '3' && c != '4' && c != '5')
            {
                System.out.print("选择错误，请重新输入：");
            }
            else
            {
                break;
            }
        }

        return c;
    }

    public static char readConfirmSelection()
    {
        char c;
        boolean inputIsValid;

        do {
            String str = readKeyBoard(1, false).toUpperCase();
            c = str.charAt(0);
            inputIsValid = (c == 'Y' || c == 'N');

            if (!inputIsValid)
            {
                System.out.print("选择错误，请重新输入：");
            }
        }
        while (!inputIsValid);

        return c;
    }

    private static String readKeyBoard(int limit, boolean blankReturn)
    {
        String line = "";

        while (scanner.hasNextLine())
        {
            line = scanner.nextLine();

            if (line.length() == 0)
            {
                if (blankReturn)
                {
                    return line;
                }

                else
                {
                    continue;
                }
            }

            if (line.length() > limit)
            {
                System.out.print("输入长度（不大于" + limit + "）错误，请重新输入：");

                continue;
            }

            break;
        }

        return line;
    }
}
