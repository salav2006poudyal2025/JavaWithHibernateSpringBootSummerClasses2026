package io.herald.MySpringWeb.Repository;

import io.herald.MySpringWeb.Model.UserTable;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//Repository -> Uses Jpa and hibernate to connect to our reqd
// database and tables
public interface UserRepository extends JpaRepository<UserTable, Integer> {

    //Custom Query
    //join queries

 boolean existsByUsernameAndPassword(String un,String pwd);
}
