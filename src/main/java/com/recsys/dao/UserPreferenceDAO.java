package com.recsys.dao;

import com.recsys.exception.DAOException;
import com.recsys.model.UserPreference;
import java.util.List;

/** DAO for {@link UserPreference} (per-user category interest weights). */
public interface UserPreferenceDAO {
    /** Returns all preferences of one user. */
    List<UserPreference> findByUser(Long userId) throws DAOException;

    /** Inserts or updates the weight (1-5) for a user and category. */
    void upsert(Long userId, Long categoryId, int weight) throws DAOException;

    /** Removes a preference. */
    void delete(Long userId, Long categoryId) throws DAOException;
}
