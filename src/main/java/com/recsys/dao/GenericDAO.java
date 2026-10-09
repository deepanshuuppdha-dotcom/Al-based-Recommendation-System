package com.recsys.dao;

import com.recsys.exception.DAOException;

import java.util.List;

/**
 * Generic DAO interface providing basic CRUD operations.
 *
 * @param <T>  Entity type
 * @param <ID> Identifier type
 */
public interface GenericDAO<T, ID> {
    T findById(ID id) throws DAOException;
    List<T> findAll() throws DAOException;
    void save(T entity) throws DAOException;
    void update(T entity) throws DAOException;
    void delete(ID id) throws DAOException;
}

