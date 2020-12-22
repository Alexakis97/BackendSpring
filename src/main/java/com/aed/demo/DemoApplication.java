package com.aed.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import com.aed.demo.repositories.UserRepository;
import com.aed.demo.fileupload.FileStorageProperties;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses= UserRepository.class)
@EnableConfigurationProperties({
	FileStorageProperties.class
})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	
	@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
	    CommonsRequestLoggingFilter requestLoggingFilter = new CommonsRequestLoggingFilter();
	    requestLoggingFilter.setIncludeClientInfo(true);
	    requestLoggingFilter.setIncludeHeaders(true);
	    requestLoggingFilter.setIncludeQueryString(true);
	    requestLoggingFilter.setIncludePayload(true);
	    requestLoggingFilter.setAfterMessagePrefix("REQUEST DATA : ");
	    requestLoggingFilter.setMaxPayloadLength(900000);
	    
	    return requestLoggingFilter;
	}

}
 