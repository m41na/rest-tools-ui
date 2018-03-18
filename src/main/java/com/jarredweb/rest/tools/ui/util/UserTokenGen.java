package com.jarredweb.rest.tools.ui.util;

import java.util.Calendar;
import java.util.Date;

import org.jasypt.util.text.BasicTextEncryptor;

public class UserTokenGen {

    private static final UserTokenGen INSTANCE = new UserTokenGen();
    private final BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
    private final int VALID_LENGTH_IN_MIN = 10;

    private UserTokenGen() {
        super();
        textEncryptor.setPassword(UserTokenGen.class.getName());
    }

    public String encrypt(String username) {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MINUTE, VALID_LENGTH_IN_MIN);
        String encryptedUsername = textEncryptor.encrypt(username);
        String encryptedTime = textEncryptor.encrypt(String.valueOf(date.getTimeInMillis()));
        return String.format("%s&&%s", encryptedUsername, encryptedTime);
    }

    public String[] decrypt(String token) {
        String[] items = token.split("&&");
        String username = textEncryptor.decrypt(items[0]);
        String date = textEncryptor.decrypt(items[1]);
        return new String[]{username, date};
    }

    public boolean expired(String token) {
        String[] parts = decrypt(token);
        long time = Long.valueOf(parts[1]);
        Calendar now = Calendar.getInstance();
        Calendar tokenExpiry = Calendar.getInstance();
        tokenExpiry.setTime(new Date(time));
        tokenExpiry.add(Calendar.MINUTE, VALID_LENGTH_IN_MIN);
        return tokenExpiry.after(now);
    }

    public static UserTokenGen getINSTANCE() {
        return INSTANCE;
    }
}
