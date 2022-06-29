package com.company.usersapi.dto;

import com.company.usersapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTOPageData {
    public List<UserDTO> content;
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

    public static UserDTOPageData convertUsersPageToUserDTOPageData(Page<User> usersPageData) {
        UserDTOPageData userDTOPageData = new UserDTOPageData();
        userDTOPageData.content = usersPageData.getContent().stream().map(UserDTO::convert).collect(Collectors.toList());
        userDTOPageData.pageable = usersPageData.getPageable();
        userDTOPageData.last = usersPageData.isLast();
        userDTOPageData.totalPages = usersPageData.getTotalPages();
        userDTOPageData.totalElements = usersPageData.getTotalElements();
        userDTOPageData.size = usersPageData.getSize();
        userDTOPageData.number = usersPageData.getNumber();
        userDTOPageData.sort = usersPageData.getSort();
        userDTOPageData.first = usersPageData.isFirst();
        userDTOPageData.numberOfElements = usersPageData.getNumberOfElements();
        userDTOPageData.empty = usersPageData.isEmpty();
        return userDTOPageData;
    }
}
