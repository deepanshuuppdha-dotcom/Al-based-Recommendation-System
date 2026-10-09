package com.recsys.dao.impl;

import com.recsys.dao.InteractionDAO;
import com.recsys.exception.DAOException;
import com.recsys.model.Interaction;
import com.recsys.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** JDBC implementation of {@link InteractionDAO}. */
public class InteractionDAOImpl implements InteractionDAO {

    @Override
    public void record(Long userId, Long productId, String type) throws DAOException {
        String sql = "INSERT INTO interactions (user_id, product_id, type) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            ps.setString(3, type);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error recording interaction", e);
        }
    }

    @Override
    public List<Interaction> findAll() throws DAOException {
        return query("SELECT * FROM interactions ORDER BY id", null);
    }

    @Override
    public List<Interaction> findByUser(Long userId) throws DAOException {
        return query("SELECT * FROM interactions WHERE user_id = ? ORDER BY created_at DESC", userId);
    }

    @Override
    public int count() throws DAOException {
        String sql = "SELECT COUNT(*) FROM interactions";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new DAOException("Error counting interactions", e);
        }
    }

    private List<Interaction> query(String sql, Long userId) {
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (userId != null) {
                ps.setLong(1, userId);
            }
            try (ResultSet rs = ps.executeQuery()) {
                List<Interaction> list = new ArrayList<>();
                while (rs.next()) {
                    Interaction i = new Interaction();
                    i.setId(rs.getLong("id"));
                    i.setUserId(rs.getLong("user_id"));
                    i.setProductId(rs.getLong("product_id"));
                    i.setType(rs.getString("type"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    i.setCreatedAt(ts == null ? null : ts.toLocalDateTime());
                    list.add(i);
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching interactions", e);
        }
    }
}
