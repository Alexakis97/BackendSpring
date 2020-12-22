package com.aed.demo.view;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Autowired
	private WebInterceptor webInterceptor;

	public void addViewControllers(ViewControllerRegistry registry) {

		registry.addViewController("/dashboard").setViewName("dashboard");
		registry.addViewController("/ekabDashboard").setViewName("ekabDashboard");
		registry.addViewController("/manageAeds").setViewName("manageAeds");
		registry.addViewController("/blank").setViewName("blank");
		registry.addViewController("/manageEvents").setViewName("manageEvents");
		registry.addViewController("/manageReports").setViewName("manageReports");
		registry.addViewController("/charts").setViewName("charts");
		registry.addViewController("/radio").setViewName("radio");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/profile").setViewName("profile");
		registry.addViewController("/403").setViewName("403");
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/success").setViewName("success");
	//	registry.addViewController("/plans").setViewName("pricingCalculator");
	//	registry.addViewController("/pricingCalculator").setViewName("pricingCalculator");

	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/profileImages/**")
				.addResourceLocations("file:home/src/main/resources/static/images/profileImages/").resourceChain(true)
				.addResolver(new PathResourceResolver());

		registry.addResourceHandler("/aedImages/**")
				.addResourceLocations("file:home/src/main/resources/static/images/aedImages/").resourceChain(true)
				.addResolver(new PathResourceResolver());

		registry.addResourceHandler("/mobileImages/**")
				.addResourceLocations("file:home/src/main/resources/static/images/mobileImages/").resourceChain(true)
				.addResolver(new PathResourceResolver());
	}

	@Bean
	public SessionLocaleResolver localeResolver() {

		SessionLocaleResolver slr = new SessionLocaleResolver();

		slr.setDefaultLocale(Locale.US);

		return slr;

	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {

		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");

		return lci;

	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(localeChangeInterceptor());
		registry.addInterceptor(webInterceptor).addPathPatterns("/dashboard","/ekab/admin","/radio","/manageReports","/manageAeds","/manageEmployees","/manageEvents","/manageAmbulance","/charts","/deleteAed","/profile","/userProfile","/","");

	}

	

}