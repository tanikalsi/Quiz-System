// Place this in the com.quizsystem.dao package
package com.quizsystem.dao;

import com.quizsystem.model.User;
import org.springframework.orm.hibernate5.HibernateTemplate;
import javax.transaction.Transactional;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private HibernateTemplate hibernateTemplate;

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    @Transactional
    public void save(User user) {
        this.hibernateTemplate.save(user);
    }
// In UserDaoImpl.java

    @Override
    public User findByUsername(String username) {
        // This is the more modern and reliable way to execute a parameterized query with HibernateTemplate
        List<User> users = hibernateTemplate.execute(session -> {
            // We use a named parameter (:uname) for better readability
            String hql = "from User where username = :uname";

            // Create the query object
            org.hibernate.query.Query<User> query = session.createQuery(hql, User.class);

            // Set the parameter value
            query.setParameter("uname", username);

            // Execute the query and return the results
            return query.list();
        });

        if (users == null || users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }
}