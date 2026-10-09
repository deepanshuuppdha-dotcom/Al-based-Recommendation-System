package com.recsys.dao;

import com.recsys.model.Product;
import com.recsys.exception.DAOException;
import java.util.List;

/**
 * DAO for {@link Product}.
 */
public interface ProductDAO extends GenericDAO<Product, Long> {
    // Additional product‑specific queries can be added here.
}
