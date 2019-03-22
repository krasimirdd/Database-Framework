package com.kdd.productshop.service.implementations;

import com.kdd.productshop.domain.dtos.ProductInRangeDTO;
import com.kdd.productshop.domain.dtos.SeedDTOs.ProductSeedDTO;
import com.kdd.productshop.domain.entities.Category;
import com.kdd.productshop.domain.entities.Product;
import com.kdd.productshop.domain.entities.User;
import com.kdd.productshop.repository.CategoryRepository;
import com.kdd.productshop.repository.ProductRepository;
import com.kdd.productshop.repository.UserRepository;
import com.kdd.productshop.service.ProductService;
import com.kdd.productshop.util.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ValidatorUtil validatorUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              UserRepository userRepository,
                              ValidatorUtil validatorUtil,
                              ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.validatorUtil = validatorUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedProducts(ProductSeedDTO[] productSeedDTOS) {
        for (ProductSeedDTO productSeedDTO : productSeedDTOS) {
            if (!this.validatorUtil.isValid(productSeedDTO)) {
                this.validatorUtil.violations(productSeedDTO)
                        .forEach(v -> System.out.println(v.getMessage()));

                continue;
            }

            Product entity = modelMapper.map(productSeedDTO, Product.class);
            entity.setSeller(this.getRandomUser());

            Random random = new Random();
            if (random.nextInt() % 13 != 0) {
                entity.setBuyer(this.getRandomUser());
            }

            entity.setCategories(this.getRandomCategorieis());

            this.productRepository.saveAndFlush(entity);
        }
    }

    @Override
    public List<ProductInRangeDTO> productsInRange(BigDecimal more, BigDecimal less) {

        List<Product> productEntities = this.productRepository
                .findAllByPriceBetweenAndBuyerOrderByPrice(more, less, null);

        List<ProductInRangeDTO> productInRangeDTOS = new ArrayList<>();

        for (Product productEntity : productEntities) {
            ProductInRangeDTO productInRangeDTO =
                    modelMapper.map(productEntity, ProductInRangeDTO.class);

            productInRangeDTO.setSeller(
                    String.format("%s %s",
                            productEntity.getSeller().getFirstName(),
                            productEntity.getSeller().getLastName()
                    ));

            productInRangeDTOS.add(productInRangeDTO);
        }

        return productInRangeDTOS;
    }

    private User getRandomUser() {
        Random random = new Random();

        return this.userRepository.getOne(
                random.nextInt(
                        (int) this.userRepository.count() - 1) + 1);
    }

    private List<Category> getRandomCategorieis() {
        Random random = new Random();

        int categoriesCount = random.nextInt((int) (this.categoryRepository.count() - 1)) + 1;

        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < categoriesCount; i++) {
            categories.add(this.categoryRepository.getOne(categoriesCount));
        }

        return categories;
    }
}
