package com.company.reviewsapi.repository;

import com.company.reviewsapi.model.Commentary;
import com.company.reviewsapi.model.CommentaryReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentaryReplyRepository extends JpaRepository<CommentaryReply, Long>, JpaSpecificationExecutor<CommentaryReply> {
    Page<CommentaryReply> findByCommentary(Commentary commentary, Pageable pageable);
}