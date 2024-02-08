package com.aston.hw2.util;

import org.mindrot.jbcrypt.BCrypt;

public class CryptoTool {

    public static String hashOf(String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }

    public static boolean isCorrectPwd(String inputPwd, String pwd) {
        System.out.println(inputPwd + " " + pwd);
        return BCrypt.checkpw(inputPwd, pwd);
    }
}
