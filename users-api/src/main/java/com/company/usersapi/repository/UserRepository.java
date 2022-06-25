package com.company.usersapi.repository;

import com.company.usersapi.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.function.Function;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> { }
