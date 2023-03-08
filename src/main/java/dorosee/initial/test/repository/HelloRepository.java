package dorosee.initial.test.repository;

import dorosee.initial.test.entity.Hello;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelloRepository extends JpaRepository<Hello, Integer> {
}
