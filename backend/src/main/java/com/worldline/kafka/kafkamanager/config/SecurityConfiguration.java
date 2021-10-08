package com.worldline.kafka.kafkamanager.config;

import com.worldline.kafka.kafkamanager.properties.InMemoryAuthenticationProperties;
import com.worldline.kafka.kafkamanager.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final InMemoryAuthenticationProperties inMemoryAuthenticationProperties;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, InMemoryAuthenticationProperties inMemoryAuthenticationProperties) {
		super(false);
		this.inMemoryAuthenticationProperties = inMemoryAuthenticationProperties;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		if (inMemoryAuthenticationProperties.isEnabled()) {

			auth.inMemoryAuthentication()
				.withUser(inMemoryAuthenticationProperties.getUserLogin())
				.password(inMemoryAuthenticationProperties.getUserPasswordHash())
				.authorities("USER")
				.and()
				.withUser(inMemoryAuthenticationProperties.getAdminLogin())
				.password(inMemoryAuthenticationProperties.getAdminPasswordHash())
				.authorities("ADMIN");
		} else {
			super.configure(auth);
		}
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.antMatchers("/login").permitAll()
			.antMatchers("/actuator/**").permitAll()
			.antMatchers("/global-settings").permitAll()
			.antMatchers("/clusters/*/metrics/**").permitAll()
			.antMatchers("/clusters/*/monitoring/**").permitAll()
			.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
