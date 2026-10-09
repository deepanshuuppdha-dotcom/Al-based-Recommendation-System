package com.recsys.dao.impl;

import com.recsys.dao.UserPreferenceDAO;
import com.recsys.exception.DAOException;
import com.recsys.model.UserPreference;
import com.recsys.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** JDBC implementation of {@link UserPreferenceDAO}. */
public class UserPreferenceDAOImpl implements UserPreferenceDAO {

    @Override
    public List<UserPreference> findByUser(Long userId) throws DAOException {
        String sql = "SELECT id, user_id, category_id, weight FROM user_preferences WHERE user_id = ? ORDER BY category_id";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<UserPreference> list = new ArrayList<>();
                while (rs.next()) {
                    UserPreference p = new UserPreference();
                    p.setId(rs.getLong("id"));
                    p.setUserId(rs.getLong("user_id"));
                    p.setCategoryId(rs.getLong("category_id"));
                    p.setWeight(rs.getInt("weight"));
                    list.add(p);
                }
                return list;
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching preferences", e);
        }
    }

    @Override
    public void upsert(Long userId, Long categoryId, int weight) throws DAOException {
        String sql = "INSERT INTO user_preferences (user_id, category_id, weight) VALUES (?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE weight = VALUES(weight)";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, categoryId);
            ps.setInt(3, weight);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error saving preference", e);
        }
    }

    @Override
    public void delete(Long userId, Long categoryId) throws DAOException {
        String sql = "DELETE FROM user_preferences WHERE user_id = ? AND category_id = ?";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, categoryId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error deleting preference", e);
        }
    }
}
