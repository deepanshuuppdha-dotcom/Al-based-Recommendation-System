package com.recsys.dao.impl;

import com.recsys.dao.ProductDAO;
import com.recsys.exception.DAOException;
import com.recsys.model.Product;
import com.recsys.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/** JDBC implementation of {@link ProductDAO}. */
public class ProductDAOImpl implements ProductDAO {

    @Override
    public Product findById(Long id) throws DAOException {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching product", e);
        }
    }

    @Override
    public List<Product> findAll() throws DAOException {
        String sql = "SELECT * FROM products ORDER BY id";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Product> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DAOException("Error fetching products", e);
        }
    }

    @Override
    public void save(Product p) throws DAOException {
        String sql = "INSERT INTO products (title, description, category_id, tags, price, image_url, popularity_score) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(ps, p);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error inserting product", e);
        }
    }

    @Override
    public void update(Product p) throws DAOException {
        String sql = "UPDATE products SET title = ?, description = ?, category_id = ?, tags = ?, price = ?, image_url = ?, popularity_score = ? WHERE id = ?";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            bind(ps, p);
            ps.setLong(8, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error updating product", e);
        }
    }

    @Override
    public void delete(Long id) throws DAOException {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error deleting product", e);
        }
    }

    private void bind(PreparedStatement ps, Product p) throws SQLException {
        ps.setString(1, p.getTitle());
        ps.setString(2, p.getDescription());
        ps.setLong(3, p.getCategoryId());
        ps.setString(4, p.getTags());
        if (p.getPrice() == null) {
            ps.setNull(5, Types.DECIMAL);
        } else {
            ps.setBigDecimal(5, BigDecimal.valueOf(p.getPrice()));
        }
        ps.setString(6, p.getImageUrl());
        ps.setDouble(7, p.getPopularityScore() == null ? 0.0 : p.getPopularityScore());
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setTitle(rs.getString("title"));
        p.setDescription(rs.getString("description"));
        p.setCategoryId(rs.getLong("category_id"));
        p.setTags(rs.getString("tags"));
        BigDecimal price = rs.getBigDecimal("price");
        p.setPrice(price == null ? null : price.doubleValue());
        p.setImageUrl(rs.getString("image_url"));
        p.setPopularityScore(rs.getDouble("popularity_score"));
        return p;
    }
}
