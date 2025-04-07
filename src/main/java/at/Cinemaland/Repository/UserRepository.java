package at.Cinemaland.Repository;

import at.Cinemaland.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Use Optional<User> to handle null values safely
    Optional<User> findByUsername(String username);
}
