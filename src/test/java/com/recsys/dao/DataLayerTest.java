package com.recsys.dao;

import com.recsys.dao.impl.CategoryDAOImpl;
import com.recsys.dao.impl.ProductDAOImpl;
import com.recsys.dao.impl.UserDAOImpl;
import com.recsys.model.Admin;
import com.recsys.model.Product;
import com.recsys.model.User;
import com.recsys.util.DBConnection;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Verifies the JDBC data layer against the seeded recsys_db database. */
class DataLayerTest {

    private int count(String table) throws Exception {
        try (Connection con = DBConnection.getDataSource().getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM " + table)) {
            rs.next();
            return rs.getInt(1);
        }
    }

    @Test
    void seededRowCounts() throws Exception {
        assertEquals(6, count("users"));
        assertEquals(40, count("products"));
        assertEquals(200, count("interactions"));
    }

    @Test
    void userDaoFindsAdmin() {
        User u = new UserDAOImpl().findById(1L);
        assertNotNull(u);
        assertEquals("admin@recsys.com", u.getEmail());
        assertInstanceOf(Admin.class, u);
    }

    @Test
    void daosReturnAllRows() {
        assertEquals(6, new UserDAOImpl().findAll().size());
        assertEquals(40, new ProductDAOImpl().findAll().size());
        assertEquals(8, new CategoryDAOImpl().findAll().size());
        Product p = new ProductDAOImpl().findById(1L);
        assertNotNull(p.getTitle());
    }
}
