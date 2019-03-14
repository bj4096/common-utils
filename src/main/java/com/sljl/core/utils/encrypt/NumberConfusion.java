package com.sljl.core.utils.encrypt;

/**
 * 数字混淆工具
 *
 * @author L.Y.F
 */
public class NumberConfusion {

    private static final char[] LETTER = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    private static final int[][] DIGIT_TABLE = {{6, 1, 3, 5, 2, 0, 8, 4, 7, 9}, {3, 7, 2, 9, 0, 5, 8, 4, 6, 1}, {7, 1, 5, 8, 9, 2, 0, 6, 3, 4}, {3, 2, 8, 7, 1, 0, 5, 6, 4, 9}, {0, 9, 5, 7, 3, 1, 2, 8, 4, 6}, {6, 4, 0, 7, 9, 3, 8, 5, 1, 2}, {6, 3, 7, 4, 1, 8, 0, 2, 9, 5}, {0, 1, 3, 2, 7, 9, 6, 8, 5, 4}, {1, 8, 7, 0, 5, 9, 2, 4, 3, 6}, {4, 7, 2, 9, 0, 8, 3, 1, 5, 6},};

    private static final Vigenere vigenere = new Vigenere();

    /**
     * 反混淆方法 -- 抛出异常
     *
     * @param ciphertext
     *
     * @return
     */
    public static final int decode(String ciphertext) {
        return decodeWithException(ciphertext);
    }

    /**
     * 反混淆方法 -- 不抛出异常
     * <p>
     * 如果反混淆抛异常，捕获不抛，返回原字符串，并且记日志
     *
     * @param ciphertext
     *
     * @return
     */
    public static final int decodeWithCompatible(String ciphertext) {
        try {
            return decodeWithException(ciphertext);
        } catch (Exception e) {
            return Integer.parseInt(ciphertext);
        }
    }

    /**
     * 混淆方法
     * <p>
     * 目前只支持小于1亿的自增长id
     *
     * @param id
     *
     * @return
     */
    public static final String encode(int id) {
        String planitext = String.valueOf(id);
        // 补齐8位
        planitext = fillup(planitext);

        // 分头尾两部分 保证固定长度
        String head = planitext.substring(0, 4);
        String foot = planitext.substring(4, 8);

        // 数字表置换
        long[] headArray = substitute(head);
        long[] footArray = substitute(foot);

        // 左移合并
        long headLong = encode(headArray);
        long footLong = encode(footArray);

        // 取种子
        int seed = (int) (headLong ^ footLong) % LETTER.length;

        // 维吉尼亚
        return vigenere.encode("" + footLong, "" + headLong) + LETTER[Math.abs(seed)] + footLong;
    }

    /**
     * 反混淆方法
     *
     * @param ciphertext
     *
     * @return
     *
     * @throws RuntimeException
     */
    private static int decodeWithException(String ciphertext) {
        // 发现种子字母
        char[] charArr = ciphertext.toCharArray();
        int pos = -1;
        char c = (char) -1;
        for (int i = 0; i < charArr.length; i++) {
            if (Character.isLetter(charArr[i])) {
                pos = i;
                c = charArr[i];
                break;
            }
        }
        if (pos == -1 || c == -1) {
            throw new RuntimeException("数据伪造");
        }

        // 校验
        String head = ciphertext.substring(0, pos);
        String foot = ciphertext.substring(pos + 1, ciphertext.length());
        long headLong = Long.parseLong(head);
        long footLong = Long.parseLong(foot);
        // 维吉尼亚
        headLong = Long.parseLong(vigenere.decode(foot, head));
        if (LETTER[Math.abs((int) (headLong ^ footLong) % LETTER.length)] != c) {
            throw new RuntimeException("数据伪造");
        }

        // 右移拆解
        String[] headArray = decode(headLong);
        String[] footArray = decode(footLong);

        // 数字表反置换
        head = unSubstitute(headArray);
        foot = unSubstitute(footArray);

        return Integer.parseInt(head + "" + foot);
    }

    /**
     * 补齐
     *
     * @param value
     *
     * @return
     */
    private static String fillup(String value) {
        StringBuilder sb = new StringBuilder();
        int i = 8 - value.length();
        for (int j = 0; j < i; j++) {
            sb.append("0");
        }
        sb.append(value);
        return sb.toString();
    }

    /**
     * 头部补零
     *
     * @param value
     *
     * @return
     */
    private static String fillupZero(String value) {
        int length = value.length();
        switch (length) {
            case 1:
                return "00" + value;
            case 2:
                return "0" + value;
            case 3:
                return value;
        }
        throw new RuntimeException("大于三位");
    }

    /**
     * 置换
     *
     * @param value
     *
     * @return
     */
    private static long[] substitute(String value) {
        char[] arr = value.toCharArray();
        long[] numArr = new long[4];
        for (int i = 0; i < arr.length; i++) {
            int x = 1;// 占位
            int y = Integer.parseInt("" + arr[i]);
            int z = DIGIT_TABLE[y][(y != 0) ? y - 1 : y];

            numArr[i] = Long.parseLong(x + "" + y + "" + z);
        }
        return numArr;
    }

    /**
     * 反置换
     *
     * @param arr
     *
     * @return
     */
    private static String unSubstitute(String[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            char[] _arr = fillupZero(arr[i]).toCharArray();
            @SuppressWarnings("unused") int x = -1;
            if (_arr.length == 2) {
                x = 0;
            } else {
                x = Integer.parseInt("" + _arr[0]);
            }

            int y = Integer.parseInt("" + _arr[1]);
            int z = Integer.parseInt("" + _arr[2]);

            if (DIGIT_TABLE[y][(y != 0) ? y - 1 : y] != z) {
                throw new RuntimeException("数据伪造");
            }
            sb.append(y);
        }
        return sb.toString();
    }

    private static long encode(long[] ip) {
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * 长整型转字符串
     *
     * @param value
     *
     * @return
     */
    private static String[] decode(long value) {
        String[] sb = new String[4];
        // 直接右移24位
        sb[0] = (String.valueOf((value >>> 24)));
        // 将高8位置0，然后右移16位
        sb[1] = (String.valueOf((value & 0x00FFFFFF) >>> 16));
        // 将高16位置0，然后右移8位
        sb[2] = (String.valueOf((value & 0x0000FFFF) >>> 8));
        // 将高24位置0
        sb[3] = (String.valueOf((value & 0x000000FF)));
        return sb;
    }

}
