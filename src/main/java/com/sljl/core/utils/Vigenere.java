package com.sljl.core.utils;

/**
 * 维尼吉亚加解密
 *
 * @author L.Y.F
 */
public class Vigenere {

    private static final String CPR_TABLE = "6493812075";

    /**
     * 生成密钥字符串
     *
     * @param key 密钥
     * @param text 明文或者密文
     *
     * @return
     */
    private String createKey(String key, String text) {
        int lengthKey = key.length();
        int lengthText = text.length();
        // 如果密钥长度与str不同，则需要生成密钥字符串
        if (lengthKey != lengthText) {
            // 如果密钥长度比str短，则以不断重复密钥的方式生成密钥字符串
            if (lengthKey < lengthText) {
                while (lengthKey < lengthText) {
                    key = key + key;
                    lengthKey = 2 * lengthKey;
                }
            }// 此时，密钥字符串的长度大于或等于str长度
            // 将密钥字符串截取为与str等长的字符串
            key = key.substring(0, lengthText);
        }
        return key;
    }

    /**
     * 加密算法
     *
     * @param key 密钥
     * @param planitext 明文
     *
     * @return
     */
    public String encode(String key, String planitext) {
        String activeKey = createKey(key, planitext);
        int len = activeKey.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int pos1 = CPR_TABLE.indexOf(planitext.charAt(i));
            if (pos1 == -1) {
                throw new RuntimeException("本算法只针对数字字符：" + planitext.charAt(i) + " 进行加密");
            }
            int pos2 = CPR_TABLE.indexOf(activeKey.charAt(i));
            if (pos2 == -1) {
                throw new RuntimeException("密钥中包含非法字符：" + activeKey.charAt(i));
            }
            int j = (pos1 + pos2) % CPR_TABLE.length();
            sb.append(CPR_TABLE.charAt(j));
        }
        return sb.toString();
    }

    /**
     * 解密算法
     *
     * @param key 密钥
     * @param ciphertext 密文
     *
     * @return
     */
    public String decode(String key, String ciphertext) {
        String activeKey = createKey(key, ciphertext);
        int len = activeKey.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int pos1 = CPR_TABLE.indexOf(activeKey.charAt(i));
            if (pos1 == -1) {
                throw new RuntimeException("密钥中包含非法字符：" + activeKey.charAt(i));
            }
            int pos2 = CPR_TABLE.indexOf(ciphertext.charAt(i));
            if (pos1 > pos2) {
                sb.append(CPR_TABLE.charAt(pos2 + CPR_TABLE.length() - pos1));
            } else {
                sb.append(CPR_TABLE.charAt(pos2 - pos1));
            }
        }
        return sb.toString();
    }

}
