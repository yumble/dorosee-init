package dorosee.initial.auth.repository;


import dorosee.initial.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username) ;
}
