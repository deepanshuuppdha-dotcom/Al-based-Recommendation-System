package com.recsys.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides a single shared HikariCP connection pool.
 * Settings are read from db.properties on the classpath (src/main/resources).
 */
public final class DBConnection {

    private static final HikariDataSource DATA_SOURCE;

    static {
        Properties props = new Properties();
        try (InputStream in = DBConnection.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("db.properties not found on classpath");
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read db.properties", e);
        }

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl(props.getProperty("url"));
        config.setUsername(props.getProperty("username"));
        config.setPassword(props.getProperty("password"));
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setPoolName("recsys-pool");
        DATA_SOURCE = new HikariDataSource(config);
    }

    private DBConnection() { }

    /**
     * Returns the shared pooled data source.
     *
     * @return the HikariCP data source
     */
    public static HikariDataSource getDataSource() {
        return DATA_SOURCE;
    }

    /** Closes the pool; call this when the web app shuts down. */
    public static void shutdown() {
        if (!DATA_SOURCE.isClosed()) {
            DATA_SOURCE.close();
        }
    }
}

