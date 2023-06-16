package com.barbosa.ms.productmgmt.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class InternationalizationConfig {
    
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
    return messageSource;
    }

    // @Bean
    // public LocalValidatorFactoryBean validatorFactoryBean() { 
    //     LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    //     bean.setValidationMessageSource(messageSource());
    //     return bean;
    // }

    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) { 
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

}
