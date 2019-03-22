package com.kdd.productshop.domain.dtos;

import com.google.gson.annotations.Expose;

import java.util.Set;

public class UserDTO {
    @Expose
    private Integer usersCount;
    @Expose
    private Set<UserDetailsDTO> userDetailsDTOS;

    public UserDTO() {
    }

    public Integer getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(Integer usersCount) {
        this.usersCount = usersCount;
    }

    public Set<UserDetailsDTO> getUserDetailsDTOS() {
        return userDetailsDTOS;
    }

    public void setUserDetailsDTOS(Set<UserDetailsDTO> userDetailsDTOS) {
        this.userDetailsDTOS = userDetailsDTOS;
    }
}
