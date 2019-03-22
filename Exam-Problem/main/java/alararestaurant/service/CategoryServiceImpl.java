package alararestaurant.service;

import alararestaurant.domain.entities.Category;
import alararestaurant.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public String exportCategoriesByCountOfItems() {
        StringBuilder exportResult = new StringBuilder();

        List<Category> categories = this.categoryRepository.exportByCount();

        categories.forEach(
                category -> {

                    exportResult.append(
                            String.format("Category: %s", category.getName())
                    )
                            .append(System.lineSeparator());

                    category.getItems().forEach(
                            item -> {

                                exportResult.append(
                                        String.format("---Item Name: %s", item.getName())
                                ).append(System.lineSeparator());

                                exportResult.append(
                                        String.format("---Item Price: %s", item.getPrice())
                                )
                                        .append(System.lineSeparator());
                                exportResult.append(System.lineSeparator());
                            }
                    );
                    exportResult.append(System.lineSeparator());
                }
        );
        return exportResult.toString().trim();
    }
}
