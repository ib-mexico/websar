package com.ibmexico.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("sessionService")
	private UserDetailsService sessionService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(sessionService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// http.authorizeRequests().antMatchers("/assets/**", "/plugins/**", "/ficheros/**","/controlPanel/Gastos/**","/controlPanel/proveedores/**", "/controlPanel/Indicadores/**","/controlPanel/DetalleMant/**",
		// "/controlPanel/cotizaciones/**","/controlPanel/oportunidadesNegocios/**","/controlPanel/entregas/**").permitAll()
			
		http.authorizeRequests().antMatchers("/assets/**", "/plugins/**", "/ficheros/**").permitAll()
				.anyRequest().authenticated()
				.and().formLogin().loginPage("/").loginProcessingUrl("/loginValidation").usernameParameter("username").passwordParameter("password").defaultSuccessUrl("/session").permitAll()
				.and().logout().logoutUrl("/logout").logoutSuccessUrl("/?logout").permitAll()
				.and().sessionManagement().invalidSessionUrl("/")
				.and().sessionManagement().maximumSessions(2).expiredUrl("/");
	}
}
