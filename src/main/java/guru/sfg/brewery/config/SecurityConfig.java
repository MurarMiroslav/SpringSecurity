package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests(authorize -> {
					authorize.antMatchers("/", "webjars/**", "/login", "/resources/**").permitAll();
					authorize.antMatchers("/beers/find").permitAll();
					authorize.antMatchers(HttpMethod.GET, "/api/v1/**").permitAll();
				})
				.authorizeRequests().anyRequest().authenticated()
				.and()
				.formLogin()
				.and()
				.httpBasic();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.inMemoryAuthentication()
				.withUser("miro")
				.password("{noop}murar")
				.roles("ADMIN")
				.and()
				.withUser("michal")
				.password("{noop}kurbel")
				.roles("USER");

	}

	@Override
	@Bean
	protected UserDetailsService userDetailsService() {

		UserDetails miro = User.withDefaultPasswordEncoder()
				.username("miro")
				.password("murar")
				.roles("ADMIN")
				.build();
		UserDetails michal = User.withDefaultPasswordEncoder()
				.username("michal")
				.password("kurbel")
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(miro, michal);
	}
}
