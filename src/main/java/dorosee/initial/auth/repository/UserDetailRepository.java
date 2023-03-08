package dorosee.initial.auth.repository;

import dorosee.initial.auth.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailRepository extends JpaRepository<UserDetail, String> {
    Optional<UserDetail> findById(String id) ;
}
