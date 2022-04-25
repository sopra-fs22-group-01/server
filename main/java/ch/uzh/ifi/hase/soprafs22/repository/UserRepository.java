package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
  //User findByName(String name);
  User findById(long id);
  User findByUsername(String username);
  User findByToken(String token);
}

//repo.save(), repo.findAll() etc.