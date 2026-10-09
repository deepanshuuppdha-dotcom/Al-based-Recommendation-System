package com.recsys.service;

import com.recsys.dao.UserDAO;
import com.recsys.dao.impl.UserDAOImpl;
import com.recsys.exception.AuthenticationException;
import com.recsys.exception.ValidationException;
import com.recsys.model.Role;
import com.recsys.model.User;
import org.mindrot.jbcrypt.BCrypt;

/** Business logic for login and registration. */
public class AuthService {
    private final UserDAO users = new UserDAOImpl();

    /** Verifies credentials; throws AuthenticationException with a generic message on failure. */
    public User login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isEmpty()) {
            throw new AuthenticationException("Email and password are required.");
        }
        User u = users.findByEmail(email.trim().toLowerCase());
        if (u == null || !Boolean.TRUE.equals(u.getIsActive())
                || !BCrypt.checkpw(password, u.getPasswordHash())) {
            throw new AuthenticationException("Invalid email or password.");
        }
        return u;
    }

    /** Validates input and creates a new USER account. */
    public User register(String name, String email, String password) {
        if (name == null || name.trim().length() < 2 || name.trim().length() > 100) {
            throw new ValidationException("Name must be 2 to 100 characters.");
        }
        if (email == null || !email.trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new ValidationException("Enter a valid email address.");
        }
        if (password == null || password.length() < 8
                || !password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) {
            throw new ValidationException("Password needs 8+ characters with a letter and a digit.");
        }
        String mail = email.trim().toLowerCase();
        if (users.findByEmail(mail) != null) {
            throw new ValidationException("That email is already registered.");
        }
        User u = new User();
        u.setName(name.trim());
        u.setEmail(mail);
        u.setPasswordHash(BCrypt.hashpw(password, BCrypt.gensalt(12)));
        u.setRole(Role.USER);
        u.setIsActive(true);
        users.save(u);
        return u;
    }
}
