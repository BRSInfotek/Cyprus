package org.cyprusbrs.install;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class TestJDBCConnection {

	public static void main(String[] args) {
		try (
				Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:orcl", "adempiere36", "adempiere36")) {

            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

	}

}
