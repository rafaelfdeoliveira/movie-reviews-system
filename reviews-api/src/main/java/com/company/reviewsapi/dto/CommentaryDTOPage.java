package com.company.reviewsapi.dto;

import com.company.reviewsapi.model.Commentary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class CommentaryDTOPage {
    public List<CommentaryDTO> content;
    public Pageable pageable;
    public boolean last;
    public int totalPages;
    public Long totalElements;
    public int size;
    public int number;
    public Sort sort;
    public boolean first;
    public int numberOfElements;
    public boolean empty;

    public static CommentaryDTOPage convertCommentariesPageToCommentaryDTOPage(Page<Commentary> commentariesPage) {
        CommentaryDTOPage commentaryDTOPage = new CommentaryDTOPage();
        commentaryDTOPage.content = commentariesPage.getContent().stream().map(CommentaryDTO::convert).collect(Collectors.toList());
        commentaryDTOPage.pageable = commentariesPage.getPageable();
        commentaryDTOPage.last = commentariesPage.isLast();
        commentaryDTOPage.totalPages = commentariesPage.getTotalPages();
        commentaryDTOPage.totalElements = commentariesPage.getTotalElements();
        commentaryDTOPage.size = commentariesPage.getSize();
        commentaryDTOPage.number = commentariesPage.getNumber();
        commentaryDTOPage.sort = commentariesPage.getSort();
        commentaryDTOPage.first = commentariesPage.isFirst();
        commentaryDTOPage.numberOfElements = commentariesPage.getNumberOfElements();
        commentaryDTOPage.empty = commentariesPage.isEmpty();
        return commentaryDTOPage;
    }
}
