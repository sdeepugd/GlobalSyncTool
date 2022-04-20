package com.evra.fullbuild.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.evra.fullbuild.data.FullBuildStatusData;
import com.evra.fullbuild.data.FullBuildTaskData;
import com.evra.remote.IpResult;

public class DatabaseManager {

    private final static Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());

    static String CREATE_FULLBUILD_STATUS_QUERY = "CREATE TABLE IF NOT EXISTS FULLBUILD_STATUS ( "
            + "ID SERIAL PRIMARY KEY NOT NULL,"
            + "ENLISTMENT_NAME TEXT NOT NULL, "
            + "BUILD_STATUS TEXT NOT NULL, "
            + "BUILD_START_TIME TEXT, "
            + "BUILD_DURATION TEXT, "
            + "BUILD_CONFIGURATION TEXT NOT NULL, "
            + "BUILD_DEVICE_TYPE TEXT NOT NULL "
            + ");";

    static String CREATE_FULLBUILD_QUEUE_QUERY = "CREATE TABLE IF NOT EXISTS FULLBUILD_QUEUE ( "
        + "ID SERIAL PRIMARY KEY NOT NULL,"
        + "ENLISTMENT_NAME TEXT NOT NULL, "
        + "BUILD_CONFIGURATION TEXT NOT NULL, "
        + "BUILD_DEVICE_TYPE TEXT NOT NULL, "
        + "TASK_STATUS TEXT NOT NULL "
        + ");";

    static String CREATE_IP_STATUS_QUERY = "CREATE TABLE IF NOT EXISTS IP_STATUS ( "
        + "ID SERIAL PRIMARY KEY NOT NULL,"
        + "IP TEXT NOT NULL, "
        + ");";

    static String UPDATE_FULLBUILD_STATUS_PREPARED_QUERY = "UPDATE FULLBUILD_STATUS SET " +
            "BUILD_STATUS = ? ," +
            "BUILD_START_TIME = ? ," +
            "BUILD_DURATION = ? ," +
            "BUILD_CONFIGURATION = ? ," +
            "BUILD_DEVICE_TYPE = ? " +
            " WHERE ENLISTMENT_NAME = ?";

    static String UPDATE_FULLBUILD_TASK_PREPARED_QUERY = "UPDATE FULLBUILD_QUEUE SET " +
            "TASK_STATUS = ? " +
            " WHERE ID = ?";
            
    static String INSERT_FULLBUILD_STATUS_PREPARED_QUERY = "INSERT INTO FULLBUILD_STATUS (ENLISTMENT_NAME, BUILD_STATUS, BUILD_START_TIME, BUILD_DURATION, BUILD_CONFIGURATION, BUILD_DEVICE_TYPE) VALUES (?, ?, ?, ?, ?, ?)";

    static String INSERT_FULLBUILD_QUEUE_PREPARED_QUERY = "INSERT INTO FULLBUILD_QUEUE (ENLISTMENT_NAME, BUILD_CONFIGURATION, BUILD_DEVICE_TYPE, TASK_STATUS) VALUES (?, ?, ?, ?)";

    static String INSERT_IP_STATUS_PREPARED_QUERY = "INSERT INTO IP_STATUS (IP) VALUES (?)";

    static String GET_TOP_FULLBUILD_QUEUE_ID_QUERY = "SELECT ID FROM FULLBUILD_QUEUE WHERE TASK_STATUS='0' LIMIT 1";
    
    static String GET_TOP_FULLBUILD_QUEUE_QUERY = "SELECT * FROM FULLBUILD_QUEUE WHERE TASK_STATUS='0' LIMIT 1";

    private static DatabaseManager instance = null;
    private Connection connection = null;

    private String dbHost = "localhost:5432";
    private String dbName = "globalsynctool";
    private String dbUser = "postgres";
    private String dbPassword = "postgres";

    public static DatabaseManager sharedInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection() {
        if (connection != null)
            return connection;
        return getConnection(dbHost, dbName, dbUser, dbPassword);
    }

    private Connection getConnection(String db_host, String db_name, String user_name, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://" + db_host + "/" + db_name, user_name,
                    password);
        LOGGER.log(Level.INFO, "getConnection");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getConnection",e);
        }

        return connection;
    }

    private DatabaseManager() {
        try {
            initConfigs();
            Statement statement = getConnection().createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeUpdate(CREATE_FULLBUILD_STATUS_QUERY);
            statement.executeUpdate(CREATE_FULLBUILD_QUEUE_QUERY);
            LOGGER.log(Level.INFO, "DatabaseManager");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "DatabaseManager",e);
            
        }
    }

    private void initConfigs() {
        String dbHostEnv = System.getenv("DBHOST");
        String dbNameEnv = System.getenv("DBNAME");
        String dbUserEnv = System.getenv("DBUSER");
        String dbPasswordEnv = System.getenv("DBPASS");
        if (dbHostEnv != null && dbHostEnv != null && dbUserEnv != null && dbPasswordEnv != null) {
            dbHost = dbHostEnv;
            dbName = dbNameEnv;
            dbUser = dbUserEnv;
            dbPassword = dbPasswordEnv;
        }
    }

    public void updateFullBuildStatus(FullBuildStatusData status) throws Exception {
        try {
            PreparedStatement statement = getConnection().prepareStatement(UPDATE_FULLBUILD_STATUS_PREPARED_QUERY);
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            String enlistmantName = status.getEnlistmentName();
            statement.setInt(1, status.getBuildStatus().getValue());
            if (status.getBuildStartTime() == null) {
                statement.setNull(2, java.sql.Types.VARCHAR);
            } else {
                statement.setString(2, status.getBuildStartTime());
            }

            if (status.getBuildDuration() == null) {
                statement.setNull(3, java.sql.Types.VARCHAR);
            } else {
                statement.setString(3, status.getBuildDuration());
            }
            statement.setInt(4, status.getBuildConfiguration().getValue());
            statement.setInt(5, status.getBuildDeviceType().getValue());
            statement.setString(6, enlistmantName);
            statement.executeUpdate();
            LOGGER.log(Level.INFO, "updateFullBuildStatus");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "updateFullBuildStatus",e);
            
            throw e;
        }
    }

    public void addFullbuildStatus(FullBuildStatusData status) throws Exception {

        try {
            PreparedStatement statement = getConnection().prepareStatement(INSERT_FULLBUILD_STATUS_PREPARED_QUERY);
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.setString(1, status.getEnlistmentName());
            statement.setInt(2, status.getBuildStatus().getValue());
            if (status.getBuildStartTime() == null) {
                statement.setNull(3, java.sql.Types.VARCHAR);
            } else {
                statement.setString(3, status.getBuildStartTime());
            }

            if (status.getBuildDuration() == null) {
                statement.setNull(4, java.sql.Types.VARCHAR);
            } else {
                statement.setString(4, status.getBuildDuration());
            }
            statement.setInt(5, status.getBuildConfiguration().getValue());
            statement.setInt(6, status.getBuildDeviceType().getValue());
            statement.executeUpdate();
            LOGGER.log(Level.INFO, "addFullbuildStatus");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "addFullbuildStatus",e);
            throw e;
        }
    }

    public List<FullBuildStatusData> getFullbuildStatus() throws Exception {
        List<FullBuildStatusData> fullbuildStatus = new ArrayList<FullBuildStatusData>();
        try {
            Statement statement = getConnection().createStatement();
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
            LOGGER.log(Level.INFO, "getFullbuildStatus");
        } catch (Exception e) {
            
            LOGGER.log(Level.SEVERE, "getFullbuildStatus",e);
            throw e;
        }
        return fullbuildStatus;
    }

    public FullBuildTaskData getTopFullBuildTask() throws Exception
    {
        FullBuildTaskData topTask = null;
        try {
            Statement statement = getConnection().createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeQuery(GET_TOP_FULLBUILD_QUEUE_QUERY);
            while (statement.getResultSet().next()) {
                topTask = new FullBuildTaskData();
                topTask.setEnlistmentName(statement.getResultSet().getString("ENLISTMENT_NAME"));
                topTask.setBuildConfiguration(
                        Integer.valueOf(statement.getResultSet().getString("BUILD_CONFIGURATION")));
                topTask.setBuildDeviceType(Integer.valueOf(statement.getResultSet().getString("BUILD_DEVICE_TYPE")));
                topTask.setTaskId(statement.getResultSet().getString("ID"));
                topTask.setTaskStatus(Integer.valueOf(statement.getResultSet().getString("TASK_STATUS")));
            }
            LOGGER.log(Level.INFO, "getTopFullBuildTask");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getTopFullBuildTask",e);
            throw e;
        }
        return topTask;
    }

    public void deleteTopFullBuildTask() throws Exception {
        try {
            Statement statement = getConnection().createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeUpdate("DELETE FROM FULLBUILD_QUEUE WHERE ID IN ("+GET_TOP_FULLBUILD_QUEUE_ID_QUERY+")");
            LOGGER.log(Level.INFO, "deleteFullBuildTask");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "deleteFullBuildTask",e);
            throw e;
        }
    }

    public void updateFullBuildTask(FullBuildTaskData task) throws Exception {
        try {
            PreparedStatement statement = getConnection().prepareStatement(UPDATE_FULLBUILD_TASK_PREPARED_QUERY);
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.setInt(1, task.getTaskStatus().getValue());
            statement.setInt(2, Integer.valueOf(task.getTaskId()));
            statement.executeUpdate();
            LOGGER.log(Level.INFO, "updateFullBuildTask");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "updateFullBuildTask for task Id: "+task.getTaskId() +" and status :"+task.getTaskStatus().getValue(),e);
            throw e;
        }
    }

    public void addFullBuildTask(FullBuildTaskData task) throws Exception {
        try {
            PreparedStatement statement = getConnection().prepareStatement(INSERT_FULLBUILD_QUEUE_PREPARED_QUERY);
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.setString(1, task.getEnlistmentName());
            statement.setInt(2, task.getBuildConfiguration().getValue());
            statement.setInt(3, task.getBuildDeviceType().getValue());
            statement.setInt(4, FullBuildTaskData.TaskStatus.CREATED.getValue());
            statement.executeUpdate();
            LOGGER.log(Level.INFO, "addFullBuildTask");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "addFullBuildTask",e);
            throw e;
        }
    }

    public void addIpStatus(IpResult ipResult) throws Exception
    {
        try {
            PreparedStatement statement = getConnection().prepareStatement(INSERT_IP_STATUS_PREPARED_QUERY);
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.setString(1, ipResult.ip);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "addIpStatus",e);
            throw e;
        }
    }

    public void deleteTopIpStatus() throws Exception
    {
        try {
            Statement statement = getConnection().createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeUpdate("DELETE FROM IP_STATUS WHERE ID IN (SELECT IP FROM IP_STATUS LIMIT 1)");
            LOGGER.log(Level.INFO, "deleteIpStatus");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "deleteIpStatus",e);
            throw e;
        }
    }

    public IpResult getIpResult() throws Exception
    {
        IpResult ipResult = null;
        try {
            Statement statement = getConnection().createStatement();
            statement.setQueryTimeout(30); // set timeout to 30 sec.
            statement.executeQuery("SELECT * FROM IP_STATUS");
            while (statement.getResultSet().next()) {
                ipResult = new IpResult();
                ipResult.ip = statement.getResultSet().getString("IP");
            }
            LOGGER.log(Level.INFO, "getIpResult");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "getIpResult",e);
            throw e;
        }
        return ipResult;
    }

}
