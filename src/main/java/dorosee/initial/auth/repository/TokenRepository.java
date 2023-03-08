package dorosee.initial.auth.repository;


import dorosee.initial.auth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {
}
