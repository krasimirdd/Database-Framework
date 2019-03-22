package com.kdd.productshop.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kdd.productshop.util.FileIOUtil;
import com.kdd.productshop.util.FileIOUtilImpl;
import com.kdd.productshop.util.ValidatorUtil;
import com.kdd.productshop.util.ValidatorUtilImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfig {

    @Bean
    public FileIOUtil fileIOUtil() {
        return new FileIOUtilImpl();
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    }

    @Bean
    public ValidatorUtil validatorUtil() {
        return new ValidatorUtilImpl();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
