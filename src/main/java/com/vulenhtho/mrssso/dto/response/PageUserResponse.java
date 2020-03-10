package com.vulenhtho.mrssso.dto.response;

import com.vulenhtho.mrssso.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageUserResponse {
    private List<UserDTO> userDTOS;

    private Integer totalPages;

    private Integer currentPage;

    public PageUserResponse(List<UserDTO> userDTOS, Integer totalPages, Integer currentPage) {
        this.userDTOS = userDTOS;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
    }
}
