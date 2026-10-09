package com.recsys.dao;

import com.recsys.exception.DAOException;
import com.recsys.model.Interaction;
import java.util.List;

/** DAO for {@link Interaction} (views, likes, clicks, purchases, dislikes). */
public interface InteractionDAO {
    /** Records one interaction. */
    void record(Long userId, Long productId, String type) throws DAOException;

    /** Returns every interaction (used by the collaborative strategy). */
    List<Interaction> findAll() throws DAOException;

    /** Returns the interactions of one user. */
    List<Interaction> findByUser(Long userId) throws DAOException;

    /** Total number of interactions. */
    int count() throws DAOException;
}
