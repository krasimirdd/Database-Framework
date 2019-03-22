package com.kdd.productshop.service.implementations;

import com.kdd.productshop.domain.dtos.*;
import com.kdd.productshop.domain.dtos.SeedDTOs.UserSeedDTO;
import com.kdd.productshop.domain.entities.Product;
import com.kdd.productshop.domain.entities.User;
import com.kdd.productshop.repository.ProductRepository;
import com.kdd.productshop.repository.UserRepository;
import com.kdd.productshop.service.UserService;
import com.kdd.productshop.util.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ValidatorUtil validatorUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ValidatorUtil validatorUtil, ModelMapper modelMapper, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.validatorUtil = validatorUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedUsers(UserSeedDTO[] userSeedDtos) {
        for (UserSeedDTO userSeedDto : userSeedDtos) {
            if (!this.validatorUtil.isValid(userSeedDto)) {
                this.validatorUtil.violations(userSeedDto)
                        .forEach(violation -> System.out.println(violation.getMessage()));

                continue;
            }

            User entity = this.modelMapper.map(userSeedDto, User.class);

            this.userRepository.saveAndFlush(entity);
        }
    }

    @Override
    public List<UserSoldProductDTO> userSoldProducts() {
        Set<User> users = this.userRepository.getUserHavingItemsSold();

        List<UserSoldProductDTO> userSoldProductDTOs = new ArrayList<>();

        for (User user : users) {
            UserSoldProductDTO userSoldProductDTO = modelMapper.map(user, UserSoldProductDTO.class);

            List<Product> products = this.productRepository.findAllBySeller_IdAndBuyerIsNotNull(user.getId());
            List<ProductDetailsDTO> productDetailsDTOS = new ArrayList<>();

            for (Product product : products) {
                ProductDetailsDTO productDetailsDTO = modelMapper.map(product, ProductDetailsDTO.class);


                if (product.getBuyer().getFirstName() != null) {
                    productDetailsDTO.setBuyerFName(product.getBuyer().getFirstName());
                }
                productDetailsDTO.setBuyerLName(product.getBuyer().getLastName());
                productDetailsDTOS.add(productDetailsDTO);

            }
            userSoldProductDTO.setProducts(productDetailsDTOS);
            userSoldProductDTOs.add(userSoldProductDTO);
        }

        return userSoldProductDTOs;
    }

    @Override
    public UserDTO getUserDTO() {
        // all users having sold item
        Set<User> users = this.userRepository.getUserHavingItemsSold();

        //usersCount : X
        UserDTO userCount = modelMapper.map(users, UserDTO.class);
        userCount.setUsersCount(users.size());

        Set<UserDetailsDTO> userDetails = new HashSet<>();

        for (User user : users) {
            /*  FirstName: X
                LastName: Y ...
            */
            UserDetailsDTO uDetails = modelMapper.map(user, UserDetailsDTO.class);
            //products with seller current user
            List<Product> products = productRepository.findAllBySeller_IdAndBuyerIsNotNull(user.getId());

            userDetails.add(uDetails);
            userCount.setUserDetailsDTOS(userDetails);

            ProductDTO productDTO = new ProductDTO();
            if (uDetails.getProductDTO() == null) {
                productDTO.setCount(this.productRepository.countProductByBuyerIsNotNull(user.getId()));

                uDetails.setProductDTO(productDTO);
            }

            Set<ProductDetailsBaseDTO> productDetails = new HashSet<>();
            productDTO.setProductDetailsDTOs(productDetails);

            for (Product product : products) {
                // ProductDetailsBaseDTO productDetailsBaseDTO = modelMapper.map(product, ProductDetailsBaseDTO.class);

                ProductDetailsBaseDTO pDetails = new ProductDetailsBaseDTO();
                pDetails.setName(product.getName());
                pDetails.setPrice(product.getPrice());
                productDetails.add(pDetails);
            }
        }

        return userCount;
    }
}
