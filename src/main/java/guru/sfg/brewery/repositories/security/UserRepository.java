package guru.sfg.brewery.repositories.security;

import guru.sfg.brewery.domain.security.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);
}
