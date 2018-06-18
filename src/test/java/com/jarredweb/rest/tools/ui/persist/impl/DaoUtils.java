package com.jarredweb.rest.tools.ui.persist.impl;

import com.jarredweb.rest.tools.ui.persist.UserAccountDao;
import com.jarredweb.rest.tools.ui.persist.entity.Account;
import com.jarredweb.rest.tools.ui.persist.entity.Profile;
import com.jarredweb.rest.tools.ui.persist.entity.AccRole;
import com.jarredweb.rest.tools.ui.persist.entity.AccStatus;
import com.jarredweb.zesty.common.bean.AppResult;
import static org.junit.Assert.assertNotNull;

import java.sql.ResultSetMetaData;

import org.springframework.jdbc.core.ResultSetExtractor;

public class DaoUtils {

	private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public static String randomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
	
	public static Profile generateAndRegisterProfile(UserAccountDao dao) {
		Profile profile = new Profile();
		profile.setEmailAddress(DaoUtils.randomAlphaNumeric(10) + "@friendmail.com");
		profile.setFirstName(DaoUtils.randomAlphaNumeric(15));
		profile.setLastName(DaoUtils.randomAlphaNumeric(15));
		AppResult<Profile> result = dao.register(profile);
		assertNotNull(result.getEntity());
		return result.getEntity();
	}
	
	public static Account generateAndRegisterAccount(UserAccountDao dao) {
		Profile profile = generateAndRegisterProfile(dao);
		Account account = new Account();
		account.setUsername(DaoUtils.randomAlphaNumeric(10));
		account.setPassword(DaoUtils.randomAlphaNumeric(15));
		account.setProfile(profile);
		account.setRole(AccRole.guest);
		account.setStatus(AccStatus.active);
		AppResult<Account> result = dao.register(account);
		assertNotNull(result.getEntity());
		return result.getEntity();
	}

	public static <T> ResultSetExtractor<T> resultSetDump() {
		return (rs) -> {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1)
						System.out.print(",  ");
					String columnValue = rs.getString(i);
					System.out.print(columnValue + " " + rsmd.getColumnName(i));
				}
				System.out.println();
			}
			return null;
		};
	}
}
