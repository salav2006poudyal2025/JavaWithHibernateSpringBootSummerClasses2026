package io.herald.MySpringWeb;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//Repository -> use Jpa and hibernate to connect to our read database and tables
public interface UserRepository extends JpaRepository<User, Long> {
}
