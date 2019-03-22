package alararestaurant.service;

import alararestaurant.domain.dtos.ItemImportJsonDTO;
import alararestaurant.domain.entities.Category;
import alararestaurant.domain.entities.Item;
import alararestaurant.repository.CategoryRepository;
import alararestaurant.repository.ItemRepository;
import alararestaurant.util.FileUtil;
import alararestaurant.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class ItemServiceImpl implements ItemService {

    private static final String ITEMS_JSON_FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/files/items.json";

    private final FileUtil fileUtil;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, FileUtil fileUtil, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson, CategoryRepository categoryRepository) {
        this.itemRepository = itemRepository;
        this.fileUtil = fileUtil;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Boolean itemsAreImported() {
        return this.itemRepository.count() > 0;
    }

    @Override
    public String readItemsJsonFile() throws IOException {
        return this.fileUtil.readFile(ITEMS_JSON_FILE_PATH);
    }

    @Override
    public String importItems(String items) {

        StringBuilder importResult = new StringBuilder();
        ItemImportJsonDTO[] itemImportJsonDTOS = this.gson.fromJson(items, ItemImportJsonDTO[].class);

        Arrays.stream(itemImportJsonDTOS)
                .forEach(itemImportJsonDTO -> {

                    Category categoryEntity = this.categoryRepository.findByName(itemImportJsonDTO.getCategory()).orElse(null);

                    if (this.validationUtil.isValid(itemImportJsonDTO)) {

                        if (categoryEntity == null) {
                            if (this.validationUtil.isValid(itemImportJsonDTO.getCategory())) {
                                categoryEntity = new Category();
                                categoryEntity.setName(itemImportJsonDTO.getCategory());
                                this.categoryRepository.saveAndFlush(categoryEntity);
                            } else {
                                return;
                            }
                        }

                        String itemName = itemImportJsonDTO.getName();
                        if (this.itemRepository.findByName(itemName).orElse(null) == null) {

                            Item itemEntity = this.modelMapper.map(itemImportJsonDTO, Item.class);
                            itemEntity.setCategory(categoryEntity);
                            this.itemRepository.saveAndFlush(itemEntity);

                            importResult.append(String.format("Record %s successfully imported.", itemEntity.getName()))
                                    .append(System.lineSeparator());

                            return;
                        } else {

                            importResult.append("Invalid data format.")
                                    .append(System.lineSeparator());

                            return;
                        }
                    } else {

                        importResult.append("Invalid data format.")
                                .append(System.lineSeparator());
                    }
                });

        return importResult.toString().trim();
    }
}
