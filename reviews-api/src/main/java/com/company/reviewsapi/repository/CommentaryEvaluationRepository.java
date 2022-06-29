package com.company.reviewsapi.repository;

import com.company.reviewsapi.model.Commentary;
import com.company.reviewsapi.model.CommentaryEvaluation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentaryEvaluationRepository extends JpaRepository<CommentaryEvaluation, Long>, JpaSpecificationExecutor<CommentaryEvaluation> {
    Page<CommentaryEvaluation> findByCommentary(Commentary commentary, Pageable pageable);
}