package io.herald.MySpringWeb.Repository;

import io.herald.MySpringWeb.Model.UserTable;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//repository -> user jpa and hibernate to connect to our read database and tables

public interface UserRepository extends JpaRepository <UserTable,Integer> {

    //custom query
    //join queries

    boolean existsByUsernameAndPassword(String username, String pwd);

}
