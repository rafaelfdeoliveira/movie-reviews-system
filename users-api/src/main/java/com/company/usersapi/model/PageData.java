package com.company.usersapi.model;

import java.util.List;

public class PageData<T> {
    public List<T> content;
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

    public static class Pageable {
        public Sort sort;
        public int offset;
        public int pageNumber;
        public int pageSize;
        public boolean paged;
        public boolean unpaged;
    }

    public static class Sort {
        public boolean empty;
        public boolean sorted;
        public boolean unsorted;
    }
}