package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserDataLoader implements CommandLineRunner {

	private final AuthorityRepository authorityRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	private void loadSecurityData() {
		Authority admin = authorityRepository.save(Authority.builder().role("ROLE_ADMIN").build());
		Authority userRole = authorityRepository.save(Authority.builder().role("ROLE_USER").build());
		Authority customer = authorityRepository.save(Authority.builder().role("ROLE_CUSTOMER").build());

		userRepository.save(User.builder()
				.username("miro")
				.password(passwordEncoder.encode("murar"))
				.authority(admin)
				.build());

		userRepository.save(User.builder()
				.username("michal")
				.password(passwordEncoder.encode("kurbel"))
				.authority(userRole)
				.build());
		userRepository.save(User.builder()
				.username("andrej")
				.password(passwordEncoder.encode("certik"))
				.authority(userRole)
				.build());
	}

	@Override
	public void run(String... args) throws Exception {
		if (authorityRepository.count() == 0) {
			loadSecurityData();
		}
	}
}
