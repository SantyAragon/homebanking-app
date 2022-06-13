package com.mindhub.HomeBanking.services;

import com.mindhub.HomeBanking.dtos.AccountDTO;
import com.mindhub.HomeBanking.models.Account;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public interface AccountService {
    List<AccountDTO> getAllAccountsDTO();

    Set<Account> getAllAccountsAuthenticated(Authentication authentication);

    Account getAccountById(Long id);
    AccountDTO getAccountDTOById(Long id);

    Account getAccountByNumber(String number);

    void saveAccount(Account account);

}
