package io.herald.MySpringWeb;

import io.herald.MySpringWeb.Model.UserTable;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//Repository -> use Jpa and hibernate to connect to our read database and tables
public interface UserRepository extends JpaRepository<UserTable, Integer> {

    boolean existsByUsernameAndPassword(String un, String pwd);
}
