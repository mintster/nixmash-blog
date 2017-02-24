package com.nixmash.blog.mvc.config;

import com.nixmash.blog.jpa.enums.DataConfigProfile;
import com.nixmash.blog.mvc.security.CurrentUserDetailsService;
import com.nixmash.blog.mvc.security.SimpleSocialUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

import static org.springframework.security.config.Elements.ANONYMOUS;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackageClasses = CurrentUserDetailsService.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String[] IGNORED_RESOURCE_LIST = new String[] {"/fonts/**", "/dashboard/**",
			"/files/**" , "/x/**", "/robots.txt" };

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	@Override
	@Profile(DataConfigProfile.H2)
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Order(2)
	@Configuration
	@Profile(DataConfigProfile.MYSQL)
	protected static class MySqlWebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

		@Autowired
		private DataSource dataSource;

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			JdbcUserDetailsManager userDetailsService = new JdbcUserDetailsManager();
			userDetailsService.setDataSource(dataSource);
			PasswordEncoder encoder = new BCryptPasswordEncoder();

			auth.userDetailsService(userDetailsService).passwordEncoder(encoder).and().jdbcAuthentication()
					.dataSource(dataSource);
		}
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(IGNORED_RESOURCE_LIST);
	}

	@Bean
	public SocialUserDetailsService socialUserDetailsService() {
		return new SimpleSocialUserDetailsService(userDetailsService());
	}

	// @formatter:off

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/admin/**")
					.access("@webUserSecurity.canAccessAdmin(authentication)")
				.antMatchers("/**").permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.anonymous()
					.principal(ANONYMOUS)
				.and()
				.formLogin()
				.loginPage("/signin")
				.loginProcessingUrl("/signin/authenticate")
				.failureHandler(authenticationFailureHandler)
				.permitAll()
				.and()
				.logout()
				.deleteCookies("remember-me")
				.permitAll()
				.and()
				.rememberMe()
				.and()
				.exceptionHandling()
				.accessDeniedPage("/403")
				.and()
				.apply(new SpringSocialConfigurer()
						.postLoginUrl("/")
						.alwaysUsePostLoginUrl(true));

	}

	// @formatter:on

}
