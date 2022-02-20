package guru.sfg.brewery.config;

import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
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
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

	@Bean
	PasswordEncoder sfgPasswordEncoderFactories() {
		return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.inMemoryAuthentication()
				.withUser("miro")
				.password("{bcrypt}$2a$10$.1gHLDljtHAjRkA81HTo0.bn1wkp3GDSpq7jfJmmQPqVyhNtRCISq")
				.roles("ADMIN")
				.and()
				.withUser("michal")
				.password("{noop}kurbel")
//				.password("{sha256}3fc951541d0d2e460b9a2f4df43a33b4e2c9a1bc4795c3cdfb1b1dbc1a47facd")  //nechapem preco nefunguje mozno preto ze to je deprecated
				.roles("USER");

	}
}
