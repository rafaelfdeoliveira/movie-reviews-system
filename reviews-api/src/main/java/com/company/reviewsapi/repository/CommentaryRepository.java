package com.company.reviewsapi.repository;

import com.company.reviewsapi.model.Commentary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, Long>, JpaSpecificationExecutor<Commentary> {
    Page<Commentary> findByMovieId(String movieId, Pageable pageable);
}