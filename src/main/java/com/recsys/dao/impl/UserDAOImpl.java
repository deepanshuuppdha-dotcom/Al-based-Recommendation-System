package com.recsys.dao.impl;

import com.recsys.dao.UserDAO;
import com.recsys.exception.DAOException;
import com.recsys.model.Role;
import com.recsys.model.User;
import com.recsys.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** JDBC implementation of {@link UserDAO}. Uses PreparedStatement everywhere. */
public class UserDAOImpl implements UserDAO {

    @Override
    public User findById(Long id) throws DAOException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching user by id", e);
        }
    }

    @Override
    public List<User> findAll() throws DAOException {
        String sql = "SELECT * FROM users ORDER BY id";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<User> list = new ArrayList<>();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new DAOException("Error fetching users", e);
        }
    }

    @Override
    public void save(User u) throws DAOException {
        String sql = "INSERT INTO users (name, email, password_hash, role, is_active) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.setString(4, u.getRole().name());
            ps.setBoolean(5, u.getIsActive() == null || u.getIsActive());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    u.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error inserting user", e);
        }
    }

    @Override
    public void update(User u) throws DAOException {
        String sql = "UPDATE users SET name = ?, email = ?, password_hash = ?, role = ?, is_active = ? WHERE id = ?";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.setString(4, u.getRole().name());
            ps.setBoolean(5, u.getIsActive() == null || u.getIsActive());
            ps.setLong(6, u.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error updating user", e);
        }
    }

    @Override
    public void delete(Long id) throws DAOException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection con = DBConnection.getDataSource().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error deleting user", e);
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        Role role = Role.valueOf(rs.getString("role"));
        User u = (role == Role.ADMIN) ? new com.recsys.model.Admin() : new User();
        u.setId(rs.getLong("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRole(role);
        u.setIsActive(rs.getBoolean("is_active"));
        return u;
    }
}
