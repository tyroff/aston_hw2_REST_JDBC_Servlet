package util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public final class DBPropertiesUtil {
    private static final String PROPERTIES_FILE = "db.properties";
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        try {
            Properties properties = loadProperties();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.username"));
            config.setPassword(properties.getProperty("db.password"));
            config.setDriverClassName(properties.getProperty("db.driver-class-name"));
            ds = new HikariDataSource(config);
        } catch (IOException e) {
            throw new RuntimeException("DBPropertiesUtil class initialization failed!", e);
        }
    }

    private DBPropertiesUtil() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();

        try (InputStream inputStream = DataSource.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (inputStream == null) {
                throw new RuntimeException("The file of " + PROPERTIES_FILE + " isn't fond!");
            }
            properties.load(inputStream);
        }
        return properties;
    }

}
