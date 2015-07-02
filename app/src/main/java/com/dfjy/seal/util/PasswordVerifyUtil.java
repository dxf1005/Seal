
package com.dfjy.seal.util;

public class PasswordVerifyUtil {

    private PasswordVerifyUtil() {

    }

    // 正则表达式纯字母
    public static final String LETTER = "^[A-Za-z]+$";
    // 正则表达式纯数字
    public static final String NUMBER = "^[0-9]+$";
    // 正则表达式包含一个字母
    public static final String SYMBOL_1 = "^.*[A-Za-z]+.*$";
    // 正则表达式包含一个数字
    public static final String SYMBOL_2 = "^.*[0-9]+.*$";

    /**
     * 判断用户名是否合法
     * 
     * @param username
     * @return
     */
    public static boolean IsUserName(String username) {
        if (username.length() < 4 || username.length() > 100) {
            return false;
        } else {
            if (username.contains(" ")) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 判断密码是否合法
     * 
     * @param str
     * @return
     */
    public static boolean IsPassWord(String password) {

        if (password.trim().length() < 6 || password.trim().length() > 16) {
            return false;
        } else {
            if (password.matches(LETTER)) {
                return false;
            } else if (password.matches(NUMBER)) {
                return false;
            } else if (password.matches(SYMBOL_1) || password.matches(SYMBOL_2)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 判断密码是否符合长度
     * 
     * @param password
     * @return
     */
    public static boolean isPasswordLength(String password) {
        if (password.trim().length() < 6 || password.trim().length() > 16) {
            return false;
        }
        return true;
    }

    /**
     * 判断用户名的长度是否符合长度
     * 
     * @param username
     * @return
     */
    public static boolean isUsernameLength(String username) {
        if (username.length() < 4 || username.length() > 100) {
            return false;
        }
        return true;
    }

    /**
     * 判断用户名的长度是否符合长度
     * 
     * @param username
     * @return
     */
    public static boolean isForgetUsernameLength(String username) {
        if (username.length() < 4 || username.length() > 50) {
            return false;
        }
        return true;
    }

}
