package com.jarredweb.rest.tools.ui.util;

import org.apache.commons.text.RandomStringGenerator;
import org.jasypt.util.password.BasicPasswordEncryptor;

public class PasswordUtil {

    private static final BasicPasswordEncryptor ENCRYPTOR = new BasicPasswordEncryptor();

    public static char[] hashPassword(char[] pwd) {
        String hashed = ENCRYPTOR.encryptPassword(new String(pwd));
        return hashed.toCharArray();
    }
    
    public static String hashPassword(String pwd) {
        String hashed = ENCRYPTOR.encryptPassword(pwd);
        return hashed;
    }

    public static boolean verifyPassword(String pwd, char[] hash) {
        boolean b = ENCRYPTOR.checkPassword(pwd, new String(hash));
        return b;
    }

    public static boolean verifyPassword(String pwd, String hash) {
        boolean b = ENCRYPTOR.checkPassword(pwd, hash);
        return b;
    }

    public static String generate() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').withinRange('A', 'Z').withinRange(0, 9).build();
        return generator.generate(20);
    }
}
