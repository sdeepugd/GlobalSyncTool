package com.evra.fullbuild.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.evra.fullbuild.data.FullBuildStatusData;

public class DatabaseManager {

    static String CREATE_FULLBUILD_STATUS_QUERY = "CREATE TABLE IF NOT EXISTS FULLBUILD_STATUS ( "
            + "ENLISTMENT_NAME TEXT NOT NULL, "
            + "BUILD_STATUS TEXT NOT NULL, "
            + "BUILD_START_TIME TEXT NOT NULL, "
            + "BUILD_DURATION TEXT NOT NULL, "
            + "BUILD_CONFIGURATION TEXT NOT NULL, "
            + "BUILD_DEVICE_TYPE TEXT NOT NULL, "
            + "PRIMARY KEY (ENLISTMENT_NAME) "
            + ");";

    private static DatabaseManager instance = null;
    private Connection connection = null;

    public static DatabaseManager sharedInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private DatabaseManager() {
        try {

            Class.forName("org.sqlite.JDBC");
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:fullbuildstatus.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeUpdate(CREATE_FULLBUILD_STATUS_QUERY);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }

    }

    public void addFullbuildStatus(FullBuildStatusData status) {
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeUpdate(
                    "INSERT INTO FULLBUILD_STATUS (ENLISTMENT_NAME, BUILD_STATUS, BUILD_START_TIME, BUILD_DURATION, BUILD_CONFIGURATION, BUILD_DEVICE_TYPE) VALUES ('"
                            +
                            status.getEnlistmentName() + "', '" +
                            status.getBuildStatus().getValue() + "', '" +
                            status.getBuildStartTime() + "', '" +
                            status.getBuildDuration() + "', '" +
                            status.getBuildConfiguration().getValue() + "', '" +
                            status.getBuildDeviceType().getValue() + "');");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public List<FullBuildStatusData> getFullbuildStatus() {
        List<FullBuildStatusData> fullbuildStatus = new ArrayList<FullBuildStatusData>();
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeQuery("SELECT * FROM FULLBUILD_STATUS");
            while (statement.getResultSet().next()) {
                FullBuildStatusData status = new FullBuildStatusData();
                status.setEnlistmentName(statement.getResultSet().getString("ENLISTMENT_NAME"));
                status.setBuildStatus(Integer.valueOf(statement.getResultSet().getString("BUILD_STATUS")));
                status.setBuildStartTime(statement.getResultSet().getString("BUILD_START_TIME"));
                status.setBuildDuration(statement.getResultSet().getString("BUILD_DURATION"));
                status.setBuildConfiguration(
                        Integer.valueOf(statement.getResultSet().getString("BUILD_CONFIGURATION")));
                status.setBuildDeviceType(Integer.valueOf(statement.getResultSet().getString("BUILD_DEVICE_TYPE")));
                fullbuildStatus.add(status);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        return fullbuildStatus;
    }

}
