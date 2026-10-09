package com.recsys.dao;

import com.recsys.dao.impl.InteractionDAOImpl;
import com.recsys.dao.impl.UserDAOImpl;
import com.recsys.dao.impl.UserPreferenceDAOImpl;
import com.recsys.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for login lookup, preferences and interactions. */
class UserFlowTest {

    @Test
    void findByEmailWorks() {
        User u = new UserDAOImpl().findByEmail("alice@example.com");
        assertNotNull(u);
        assertNull(new UserDAOImpl().findByEmail("nobody@example.com"));
    }

    @Test
    void searchFindsUsers() {
        assertFalse(new UserDAOImpl().search("alice").isEmpty());
    }

    @Test
    void preferenceUpsertRoundTrip() {
        UserPreferenceDAO dao = new UserPreferenceDAOImpl();
        int before = dao.findByUser(2L).size();
        dao.upsert(2L, 1L, 4);
        assertEquals(before, dao.findByUser(2L).size());
    }

    @Test
    void interactionsAtLeastSeeded() {
        assertTrue(new InteractionDAOImpl().count() >= 200);
    }
}
