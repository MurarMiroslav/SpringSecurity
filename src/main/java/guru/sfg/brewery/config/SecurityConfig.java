package guru.sfg.brewery.config;

import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests(authorize -> {
					authorize.antMatchers("/h2-console/**").permitAll();
					authorize.antMatchers("/", "webjars/**", "/login", "/resources/**").permitAll();
					authorize.antMatchers("/beers/find").permitAll();
					authorize.antMatchers(HttpMethod.GET, "/api/v1/**").permitAll();
				})
				.authorizeRequests().anyRequest().authenticated()
				.and()
				.formLogin()
				.and()
				.httpBasic()
				.and().csrf().disable();

		http.headers().frameOptions().sameOrigin();
	}

	@Bean
	PasswordEncoder sfgPasswordEncoderFactories() {
		return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
