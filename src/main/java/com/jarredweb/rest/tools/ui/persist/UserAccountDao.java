package com.jarredweb.rest.tools.ui.persist;

import com.jarredweb.rest.tools.ui.common.AppResult;
import com.jarredweb.rest.tools.ui.persist.entity.Account;
import com.jarredweb.rest.tools.ui.persist.entity.Profile;
import java.util.List;

public interface UserAccountDao {

    AppResult<List<Account>> retrieveAccounts(int start, int size);

    AppResult<Account> findAccount(long id);

    AppResult<Account> findByUsername(String username);

    AppResult<Account> register(Account account);

    AppResult<Account> update(Account account);

    AppResult<Account> resetPassword(Account account);

    AppResult<Integer> deleteAccount(Long id);

    AppResult<Profile> findProfile(long id);

    AppResult<Profile> findByEmail(String email);

    AppResult<Profile> register(Profile profile);

    AppResult<Profile> update(Profile profile);

    AppResult<Integer> deleteProfile(Long id);
}
