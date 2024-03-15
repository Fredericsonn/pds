package uiass.gisiba.eia.java.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import uiass.gisiba.eia.java.entity.crm.Contact;

// Singleton pattern for SessionFactory

public class GetSessionFactory {

    private static SessionFactory sessionFactory;

    private GetSessionFactory(){}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {

            // Create Configuration
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.setProperty("hibernate.connection.autocommit", "true");
            
            configuration.addAnnotatedClass(Contact.class);
           
            // Create Session Factory
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }
}
