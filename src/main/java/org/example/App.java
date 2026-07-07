package org.example;

import org.example.model.AddressClass;
import org.example.model.User2Class;
import org.example.model.UserClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

//        System.out.println( "Hello World!" );

        UserClass userClass = new UserClass();

        User2Class uc2 =  new User2Class();
        uc2.setPhone("1321321321");

        AddressClass ac = new AddressClass();
        ac.setTown("Naxal");
        ac.setUser(userClass);

//        userClass.setId(1);
        userClass.setFullName("Herald College Kathmandu");

        //session factory activate from config
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        Session session = factory.openSession();

        session.persist(userClass);  //save
        session.persist(uc2);
        session.persist(ac);

        session.beginTransaction().commit();  //execute
        session.close();
    }
}
