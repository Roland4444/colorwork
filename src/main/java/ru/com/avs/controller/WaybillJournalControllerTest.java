package ru.com.avs.controller;

import fr.roland.DB.Executor;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class WaybillJournalControllerTest {
    public String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    public String protocol = "jdbc:derby:";

    @org.junit.Test
    public void initialize() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
        assertEquals(2,2);

        Class.forName(driver).newInstance();
        Connection conn = DriverManager.getConnection(protocol + "derbyDB;create=true", null);
        assertEquals(null, conn);



    }

    @Test
    public void testInitialize() throws SQLException {
        Executor exec = new Executor(protocol + "derbyDB;create=true");
        assertNotEquals(null, exec);
        ResultSet res = exec.submit("SELECT * FROM derbyDB.SCALES");
        assertNotEquals(res, null);


    }
}