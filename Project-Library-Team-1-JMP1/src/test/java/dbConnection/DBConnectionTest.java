package dbConnection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnectionTest {

    DBConnectionManager instance;

    @Before
    public void instanceSetUp() {
        instance = DBConnectionManager.getInstance();
    }

    @Test
    public void testGetInstance() {
        //instance = DBConnectionManager.getInstance();
        Assert.assertNotEquals(null, instance);
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = instance.getConnection();
        Assert.assertNotEquals(null, connection);
    }

    @Test
    public void testInsertQuery() throws SQLException {
        Connection connection = instance.getConnection();

        String insertQuery = "INSERT INTO genre(genreID, genreName) VALUES(?, ?)";
        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
        insertStatement.setString( 1,"37");
        insertStatement.setString( 2,"Comedy");
        int rowsInserted = insertStatement.executeUpdate();
        System.out.println(rowsInserted + " row(s) inserted.");
    }

    @Test
    public void testSelectQuery() throws SQLException {
        Connection connection = instance.getConnection();

        String selectQuery = "SELECT * FROM role";
        PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
        ResultSet resultSet = selectStatement.executeQuery();
        while (resultSet.next()) {
            // Process each row
            String column1Value = resultSet.getString("userRoleID");
            String columnRole = resultSet.getString("userRole");
            System.out.println("UserRoleID: " + column1Value + " | Role: " + columnRole);
        }
    }
}
