package ru.com.avs.config;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ShutdownBaseBean {

    private static final Logger log = LoggerFactory.getLogger(ShutdownBaseBean.class);

    @Value("${jdbc.url}")
    private String jdbcUrl;

    /**
     * shutdown base data.
     */
    public void shutdown() {
        try {
            DriverManager.getConnection(jdbcUrl + ";shutdown=true");
        } catch (SQLException e) {
            log.info("shutdown");
        }
    }
}
