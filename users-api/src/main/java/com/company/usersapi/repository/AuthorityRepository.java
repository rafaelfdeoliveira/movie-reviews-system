package com.company.usersapi.repository;

import com.company.usersapi.model.Authority;
import com.company.usersapi.model.AuthorityKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, AuthorityKey> { }
