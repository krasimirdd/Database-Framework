package com.kdd.cardealer.config;

import com.kdd.cardealer.util.ValidationUtil;
import com.kdd.cardealer.util.ValidationUtilImpl;
import com.kdd.cardealer.util.XmlParser;
import com.kdd.cardealer.util.XmlParserImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfig {

    @Bean
    public ValidationUtil validationUtil() {
        return new ValidationUtilImpl();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public XmlParser xmlParser() {
        return new XmlParserImpl();
    }
}
