package com.company.reviewsapi.repository;

import com.company.reviewsapi.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeRepository extends JpaRepository<Grade, String>, JpaSpecificationExecutor<Grade> {
    List<Grade> findByMovieId(String movieId);
}