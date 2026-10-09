package com.recsys.dao;

import com.recsys.model.User;
import com.recsys.exception.DAOException;
import java.util.List;

/**
 * DAO for {@link User}.
 */
public interface UserDAO extends GenericDAO<User, Long> {
    // Additional user-specific queries can be added here.
}
