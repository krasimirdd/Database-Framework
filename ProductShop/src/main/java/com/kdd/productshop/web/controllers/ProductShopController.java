package com.kdd.productshop.web.controllers;

import com.google.gson.Gson;
import com.kdd.productshop.domain.dtos.CategoryByProductsDTO;
import com.kdd.productshop.domain.dtos.ProductInRangeDTO;
import com.kdd.productshop.domain.dtos.SeedDTOs.CategorySeedDTO;
import com.kdd.productshop.domain.dtos.SeedDTOs.ProductSeedDTO;
import com.kdd.productshop.domain.dtos.SeedDTOs.UserSeedDTO;
import com.kdd.productshop.domain.dtos.UserDTO;
import com.kdd.productshop.domain.dtos.UserSoldProductDTO;
import com.kdd.productshop.service.CategoryService;
import com.kdd.productshop.service.ProductService;
import com.kdd.productshop.service.UserService;
import com.kdd.productshop.util.FileIOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.io.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class ProductShopController implements CommandLineRunner {

    private final static String USER_FILE_PATH = "D:\\IntelliJ_Projects\\DatabasesFrameworks\\ProductShop\\src\\main\\resources\\files\\users.json";
    private final static String CATEGORY_FILE_PATH = "D:\\IntelliJ_Projects\\DatabasesFrameworks\\ProductShop\\src\\main\\resources\\files\\categories.json";
    private final static String PRODUCT_FILE_PATH = "D:\\IntelliJ_Projects\\DatabasesFrameworks\\ProductShop\\src\\main\\resources\\files\\products.json";

    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final FileIOUtil fileUtil;
    private final Gson gson;


    @Autowired
    public ProductShopController(UserService userService, CategoryService categoryService, ProductService productService, FileIOUtil fileUtil, Gson gson) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.fileUtil = fileUtil;
        this.gson = gson;
    }

    @Override
    public void run(String... args) throws Exception {

        /** 2. Seed The Database
         *
         seedUsers();
         seedCategories();
         seedProducts();
         *
         **/

        /* 3. Query and Export Data */

        // Query 1-> productsInRange();
        // Query 2-> successfullySoldProducts();
        // Query 3-> categoriesByProductsCount();
        // Query 4-> usersAndProducts();
    }

    private void usersAndProducts() {
        UserDTO userDTO = this.userService.getUserDTO();

        String userDTOJSON = this.gson.toJson(userDTO);

        System.out.println(userDTOJSON);
    }

    private void categoriesByProductsCount() {
        List<CategoryByProductsDTO> categoryByProductsDTOS = this.categoryService.getAllCategories();

        Collections.sort(
                categoryByProductsDTOS,
                Comparator.comparing(
                        CategoryByProductsDTO::getProductsCount).reversed()
        );

        String categoriesByProductsInJSON = this.gson.toJson(categoryByProductsDTOS);
        System.out.println(categoriesByProductsInJSON);
    }

    private void successfullySoldProducts() {
        List<UserSoldProductDTO> userSoldProductDTOs = this.userService.userSoldProducts();

        Collections.sort(
                userSoldProductDTOs,
                Comparator.comparing(UserSoldProductDTO::getLastName)
        );

        String userSoldProductsJSON = this.gson.toJson(userSoldProductDTOs);

        System.out.println(userSoldProductsJSON);
    }

    private void productsInRange() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] input = reader.readLine().split("\\s+");

        List<ProductInRangeDTO> productInRangeDTOS = this.productService
                .productsInRange(
                        BigDecimal.valueOf(Long.parseLong(input[0])),
                        BigDecimal.valueOf(Long.parseLong(input[1]))
                );

        String productsInRangeJSON = this.gson.toJson(productInRangeDTOS);

        System.out.println(productsInRangeJSON);

        /**
         *  <! WRITING IN JSON FILE >
         *
         *  String filePath = "D:\\IntelliJ_Projects\\DatabasesFrameworks\\ProductShop\\src\\main\\resources\\files\\output\\products-in-range.json";
         *  writeInFile(productsInRangeJSON, filePath);
         */
    }

    private void writeInFile(String productsInRangeJSON, String path) throws IOException {
        File file = new File(path);
        FileWriter writer = new FileWriter(file);
        writer.write(productsInRangeJSON);
        writer.close();
    }

    private void seedUsers() throws IOException {
        String usersFileContent = this.fileUtil.readFile(USER_FILE_PATH);

        UserSeedDTO[] userSeedDtos = this.gson.fromJson(usersFileContent, UserSeedDTO[].class);

        this.userService.seedUsers(userSeedDtos);
    }

    private void seedCategories() throws IOException {
        String categoriesFileContent = this.fileUtil.readFile(CATEGORY_FILE_PATH);

        CategorySeedDTO[] categorySeedDTOS = this.gson.fromJson(categoriesFileContent, CategorySeedDTO[].class);

        this.categoryService.seedCategories(categorySeedDTOS);
    }

    private void seedProducts() throws IOException {
        String productsFileContent = this.fileUtil.readFile(PRODUCT_FILE_PATH);

        ProductSeedDTO[] productSeedDTOS = this.gson.fromJson(productsFileContent, ProductSeedDTO[].class);

        this.productService.seedProducts(productSeedDTOS);
    }


}

