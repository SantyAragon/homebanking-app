package com.mindhub.HomeBanking.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AccountAndDateDTO {
    String numberAccount;
    LocalDateTime since;
    LocalDateTime until;

    public AccountAndDateDTO() {
    }

    public AccountAndDateDTO(String numberAccount, LocalDateTime since, LocalDateTime until) {
        this.numberAccount = numberAccount;
        this.since = since;
        this.until = until;
    }

    public String getNumberAccount() {
        return numberAccount;
    }

    public void setNumberAccount(String numberAccount) {
        this.numberAccount = numberAccount;
    }

    public LocalDateTime getSince() {
        return since;
    }

    public void setSince(LocalDateTime since) {
        this.since = since;
    }

    public LocalDateTime getUntil() {
        return until;
    }

    public void setUntil(LocalDateTime until) {
        this.until = until;
    }
}
