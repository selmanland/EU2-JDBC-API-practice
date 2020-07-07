package jdbctests;

import org.testng.annotations.Test;

import java.sql.*;

public class jdbc_example {

    String dbUrl ="jdbc:oracle:thin:@54.210.58.84:1521:xe";
    String dbUsername = "hr";
    String dbPassword = "hr";

    @Test
    public void test1() throws SQLException {

        //create the connection
        Connection connection = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);
        //create statement object
        Statement statement = connection.createStatement();
        //run query and get the result in resultset object
        ResultSet resultSet = statement.executeQuery("select * from regions");

        while(resultSet.next()){
            System.out.println(resultSet.getString(2));
        }

        //=========================

        resultSet = statement.executeQuery("select * from departments");

        while(resultSet.next()){
            System.out.println(resultSet.getString(2));
        }

        //closing all connections
        resultSet.close();
        statement.close();
        connection.close();

    }

    @Test
    public void CountAndNavigate() throws SQLException {


        //create the connection
        Connection connection = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);
        //create statement object
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        //run query and get the result in resultset object
        ResultSet resultSet = statement.executeQuery("select * from departments");

        //how to find how many rows we have for the query
        //go to last row
        resultSet.last();
        //get the row count
        int rowCount = resultSet.getRow();

        System.out.println(rowCount);
        //we need move before first row to get full list since we are at the last row right now
         resultSet.beforeFirst();
        while(resultSet.next()){
            System.out.println(resultSet.getString(2));
        }

        //closing all connections
        resultSet.close();
        statement.close();
        connection.close();


    }

    @Test
    public void metadata() throws SQLException {
        //create the connection
        Connection connection = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);
        //create statement object
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        //run query and get the result in resultset object
        ResultSet resultSet = statement.executeQuery("select * from countries");

        //get the database related data inside the dbMetadata object
        DatabaseMetaData dbMetadata = connection.getMetaData();

        System.out.println("User= "+dbMetadata.getUserName());
        System.out.println("Database Product Name = " + dbMetadata.getDatabaseProductName());
        System.out.println("Database Product version = " + dbMetadata.getDatabaseProductVersion());
        System.out.println("Driver =" + dbMetadata.getDriverName());
        System.out.println("Driver =" + dbMetadata.getDriverVersion());

        //get the resultset object metadata
        ResultSetMetaData rsMetadata = resultSet.getMetaData();

        //how many columns we have ?
        System.out.println("Column count = " + rsMetadata.getColumnCount());

        //column names
       // System.out.println(rsMetadata.getColumnName(4));

        //print all the column names dynamically
        int colCount = rsMetadata.getColumnCount();

        for (int i = 1; i <= colCount ; i++) {
            System.out.println(rsMetadata.getColumnName(i));
        }

        //closing all connections
        resultSet.close();
        statement.close();
        connection.close();

    }

}
