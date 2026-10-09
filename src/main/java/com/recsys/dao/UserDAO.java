package com.recsys.dao;

import com.recsys.model.User;
import com.recsys.exception.DAOException;
import java.util.List;

/** DAO for {@link User}. */
public interface UserDAO extends GenericDAO<User, Long> {
    /**
     * Finds a user by e-mail (used for login).
     * @param email the e-mail address
     * @return the user, or null when not found
     */
    User findByEmail(String email) throws DAOException;

    /**
     * Searches users by name or e-mail (admin user list).
     * @param term search text; empty returns everyone
     */
    List<User> search(String term) throws DAOException;
}
