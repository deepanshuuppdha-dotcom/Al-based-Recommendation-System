package com.recsys.dao.impl;

import com.recsys.dao.CategoryDAO;
import com.recsys.exception.DAOException;
import com.recsys.model.Category;
import com.recsys.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** JDBC implementation of {@link CategoryDAO}. */
public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public Category findById(Long id) throws DAOException {
        String sql = "SELECT id, name FROM categories WHERE id = ?";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching category", e);
        }
    }

    @Override
    public List<Category> findAll() throws DAOException {
        String sql = "SELECT id, name FROM categories ORDER BY name";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Category> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DAOException("Error fetching categories", e);
        }
    }

    @Override
    public void save(Category c) throws DAOException {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error inserting category", e);
        }
    }

    @Override
    public void update(Category c) throws DAOException {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setLong(2, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error updating category", e);
        }
    }

    @Override
    public void delete(Long id) throws DAOException {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error deleting category", e);
        }
    }

    private Category mapRow(ResultSet rs) throws SQLException {
        Category c = new Category();
        c.setId(rs.getLong("id"));
        c.setName(rs.getString("name"));
        return c;
    }
}
