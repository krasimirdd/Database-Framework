package com.kdd.productshop.service;

import com.kdd.productshop.domain.dtos.SeedDTOs.UserSeedDTO;
import com.kdd.productshop.domain.dtos.UserDTO;
import com.kdd.productshop.domain.dtos.UserSoldProductDTO;

import java.util.List;

public interface UserService {

    void seedUsers(UserSeedDTO[] userSeedDtos);

    List<UserSoldProductDTO> userSoldProducts();

    UserDTO getUserDTO();

}
