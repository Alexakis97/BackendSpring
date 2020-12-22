package com.aed.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

@EnableWebSecurity
public class MultipleSecurity {

	@Autowired
	private UserDetailsService myUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService).passwordEncoder(new CustomPasswordEncoder());
	}

	// @Bean
	// public PasswordEncoder PasswordEncoder()
	// {

	// return NoOpPasswordEncoder.getInstance();
	// }

	@Order(1)
	@Configuration
	public class SecurityConfigurationApi extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity httpSecurity) throws Exception {
			httpSecurity.csrf().disable().antMatcher("/api/**").authorizeRequests()

					.antMatchers("/api/users").hasAuthority("ADMIN")
					.antMatchers("/api/authenticate/android", "/api/users/login", "/api/users/register",
							"/api/users/social/register")
					.permitAll()
					.antMatchers("/css/**", "/js/**", "/images/**", "/error_css/**", "/profileImages/**",
							"/aedImages/**", "/mobileImages/**", "/scss/**", "/vendor/**")
					.permitAll().antMatchers("/geojons/**").permitAll().anyRequest().authenticated().and()
					.exceptionHandling().accessDeniedPage("/403").and()
					.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		}
	}

	@Order(2)
	@Configuration
	public static class SecurityConfigurationWeb extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity httpSecurity) throws Exception {
			httpSecurity.csrf().disable().authorizeRequests().antMatchers("/charts")
					.hasAnyAuthority("ADMIN", "STELEXOS", "MASTER_CLINIC").antMatchers("/ekab/admin")
					.hasAnyAuthority("ADMIN", "STELEXOS", "MASTER_CLINIC").antMatchers("/")
					.hasAnyAuthority("EKAB_ADMIN", "ADMIN", "MASTER_CLINIC").antMatchers("/manageEmployees")
					.hasAnyAuthority("EKAB_ADMIN", "ADMIN", "STELEXOS", "MASTER_CLINIC").antMatchers("/admin")
					.hasAuthority("ADMIN").antMatchers("/profile").hasAnyAuthority("ADMIN", "MASTER_CLINIC")
					.antMatchers("/dashboard").hasAnyAuthority("ADMIN", "STELEXOS", "TILEFONITIS", "MASTER_CLINIC")
					.antMatchers("/manageAeds").hasAnyAuthority("ADMIN", "STELEXOS", "TILEFONITIS", "MASTER_CLINIC")
					.antMatchers("/manageEvents").hasAnyAuthority("ADMIN", "STELEXOS", "MASTER_CLINIC")
					.antMatchers("/manageAmbulance").hasAnyAuthority("ADMIN", "STELEXOS", "MASTER_CLINIC")
					.antMatchers("/manageReports").hasAnyAuthority("ADMIN", "STELEXOS", "TILEFONITIS", "MASTER_CLINIC")
					.antMatchers("/radio").hasAnyAuthority("ADMIN", "ASIRMATISTIS", "STELEXOS", "MASTER_CLINIC")
					.antMatchers("/userProfile").hasAnyAuthority("ADMIN", "ASIRMATISTIS", "STELEXOS", "MASTER_CLINIC")
					.antMatchers("/communications").hasAnyAuthority("ADMIN", "STELEXOS", "MASTER_CLINIC")
					.antMatchers("/announcement").hasAnyAuthority("ADMIN", "STELEXOS", "MASTER_CLINIC")
					.antMatchers("/createAnnouncement").hasAnyAuthority("ADMIN", "STELEXOS", "MASTER_CLINIC")
					.antMatchers("/chats").hasAnyAuthority("ADMIN", "STELEXOS", "MASTER_CLINIC")

					.antMatchers("/pricingCalculator").permitAll()

					.antMatchers("/plans").hasAnyAuthority("ADMIN", "MASTER_CLINIC").antMatchers("/subscription")
					.hasAnyAuthority("ADMIN", "MASTER_CLINIC").antMatchers("/create-subscription")
					.hasAnyAuthority("ADMIN", "MASTER_CLINIC").antMatchers("/success")
					.hasAnyAuthority("ADMIN", "MASTER_CLINIC").antMatchers("/web/marketing/channel")
					.hasAnyAuthority("ADMIN", "ASIRMATISTIS", "STELEXOS", "TILEFONITIS")
					.antMatchers("/web/profile/image/upload")
					.hasAnyAuthority("ADMIN", "ASIRMATISTIS", "STELEXOS", "TILEFONITIS")
					.antMatchers("/web/events/receive/**")
					.hasAnyAuthority("ADMIN", "ASIRMATISTIS", "STELEXOS", "TILEFONITIS")
					.antMatchers("/web/ambulance/receive")
					.hasAnyAuthority("ADMIN", "ASIRMATISTIS", "STELEXOS", "TILEFONITIS")
					.antMatchers("/web/ambulance/add")
					.hasAnyAuthority("ADMIN", "ASIRMATISTIS", "STELEXOS", "TILEFONITIS").antMatchers("/web/events/add")
					.hasAnyAuthority("ADMIN", "ASIRMATISTIS", "STELEXOS", "TILEFONITIS")
					.antMatchers("/web/asirmatistis/add")
					.hasAnyAuthority("ADMIN", "ASIRMATISTIS", "STELEXOS", "TILEFONITIS")
					.antMatchers("/web/completed/event")
					.hasAnyAuthority("ADMIN", "ASIRMATISTIS", "STELEXOS", "TILEFONITIS")
					.antMatchers("/web/completed/receive")
					.hasAnyAuthority("ADMIN", "ASIRMATISTIS", "STELEXOS", "TILEFONITIS")
					.antMatchers("/web/asirmatistis/receive")
					.hasAnyAuthority("ADMIN", "ASIRMATISTIS", "STELEXOS", "TILEFONITIS")
					.antMatchers("/web/ambulance/assign/add")
					.hasAnyAuthority("ADMIN", "ASIRMATISTIS", "STELEXOS", "TILEFONITIS")

					.antMatchers("/src/main/resources/**").permitAll()

					.and().formLogin().loginPage("/login").defaultSuccessUrl("/redirect").and().logout()

					.permitAll().and().exceptionHandling().accessDeniedPage("/403");

			httpSecurity.headers().frameOptions().disable().addHeaderWriter(
					new StaticHeadersWriter("Content-Security-Policy", "frame-ancestors http://localhost:8081"));

			httpSecurity.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true)
					.sessionRegistry(sessionRegistry()).and().invalidSessionUrl("/login?token=expired");
		}

		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {

			return super.authenticationManagerBean();
		}

		// Work around https://jira.spring.io/browse/SEC-2855
		@Bean
		public SessionRegistry sessionRegistry() {
			SessionRegistry sessionRegistry = new SessionRegistryImpl();
			return sessionRegistry;
		}

		// Register HttpSessionEventPublisher
		@Bean
		public static ServletListenerRegistrationBean httpSessionEventPublisher() {
			return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
		}

	}

}
