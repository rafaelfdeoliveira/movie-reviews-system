package com.company.reviewsapi.repository;

import com.company.reviewsapi.model.Commentary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, String>, JpaSpecificationExecutor<Commentary> {
    List<Commentary> findByMovieId(String movieId);
}