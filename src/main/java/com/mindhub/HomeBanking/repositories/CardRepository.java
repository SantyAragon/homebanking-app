package com.mindhub.HomeBanking.repositories;

import com.mindhub.HomeBanking.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByNumber(String number);
}