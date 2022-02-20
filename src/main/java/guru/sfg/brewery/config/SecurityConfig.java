package guru.sfg.brewery.config;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
		RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
		filter.setAuthenticationManager(authenticationManager);
		return filter;
	}

	public RestUrlAuthFilter restUrlAuthFilter(AuthenticationManager authenticationManager) {
		RestUrlAuthFilter filter = new RestUrlAuthFilter(new AntPathRequestMatcher("/api/**"));
		filter.setAuthenticationManager(authenticationManager);
		return filter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class).csrf().disable();

		http.addFilterBefore(restUrlAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);

		http.authorizeRequests(authorize -> {
					authorize.antMatchers("/", "webjars/**", "/login", "/resources/**").permitAll();
					authorize.antMatchers("/beers/find").permitAll();
					authorize.antMatchers(HttpMethod.GET, "/api/v1/**").permitAll();
//					authorize.antMatchers(HttpMethod.DELETE, "/api/v1/**").permitAll();		//Ak vypnes filtre a chces aby testy presli odkomentuj
				})
				.authorizeRequests().anyRequest().authenticated()
				.and()
				.formLogin()
				.and()
				.httpBasic()

//				.and().csrf().disable().cors()			//Ak vypnes filtre a chces aby testy presli odkomentuj
				;
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
				.roles("USER");
	}
}
