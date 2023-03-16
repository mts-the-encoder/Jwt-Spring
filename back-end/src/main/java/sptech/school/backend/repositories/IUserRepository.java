package sptech.school.backend.repositories;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sptech.school.backend.entities.User;
import sptech.school.backend.entities.enums.Role;

public interface IUserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  Optional<User> findByFirstName(String firstName);

  List<User> findAllByRole(Role role);
}
