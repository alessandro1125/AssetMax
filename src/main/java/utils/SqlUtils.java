package utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SqlUtils {

    /**
     *
     * @param connection Connection
     * @param query String
     * @return boolean
     */
    public static boolean sqlCreateTable(Connection connection, String query){
        /*
        String query = "CREATE TABLE assetMaxUsers (" +
                "ID int PRIMARY KEY AUTO_INCREMENT," +
                "NAME VARCHAR(255), " +
                "EMAIL VARCHAR(255)," +
                "ACCOUNT_ID VARCHAR(255)," +
                "ACTIVE VARCHAR(255)," +
                "ATTIVO VARCHAR(255)," +
                "PASSKEY VARCHAR(255));";*/
        return executeQuery(connection, query);
    }

    public static boolean sqlAdd(Connection connection, HashMap<String, String> record, String table){
        String keys, values;
        StringBuilder keysBuilder, valuesBuilder;
        keysBuilder = new StringBuilder();
        valuesBuilder = new StringBuilder();
        for (String key : record.keySet()){
            keysBuilder = keysBuilder.append(key).append(" ,");

            if (record.get(key).getClass() == String.class)
                valuesBuilder.append("'");
            valuesBuilder = valuesBuilder.append(record.get(key));
            if (record.get(key).getClass() == String.class)
                valuesBuilder.append("'");
            valuesBuilder = valuesBuilder.append(" ,");
        }
        keys = keysBuilder.toString();
        keys = keys.substring(0, keys.length()-2);

        values = valuesBuilder.toString();
        values = values.substring(0, values.length()-2);

        String query = "INSERT INTO " + table + " (" + keys + ")" +
                " VALUES (" + values + ");";
        return executeQuery(connection, query);
    }

    /**
     *
     * @param connection Connection
     * @param table String
     * @param columns ArrayList<Stirng, String>
     * @param params String
     * @return ResultSet
     */
    public static ResultSet sqlSelect(Connection connection, String table,
                                      ArrayList<String> columns, String params){

        //Faccio una chiamata al db
        Statement statement;
        String query;

        //Build the query
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ");
        if(columns != null) {
            for (int i = 0; i < columns.size(); i++) {
                builder.append(columns.get(i));
                if (i != columns.size() - 1)
                    builder.append(",");
            }
        }else {
            builder.append("*");
        }
        builder.append(" FROM ");
        builder.append(table);
        builder.append(" WHERE ");
        builder.append(params);
        builder.append(";");
        query = builder.toString();

        try{
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            connection.close();
            if (resultSet != null){
                return resultSet;
            }else {
                return null;
            }
        }catch (Exception sqle){
            System.out.println(sqle.toString());
            sqle.printStackTrace();
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     *
     * @param connection Connection
     * @param table String
     * @param columns ArrayList<Stirng, String>
     * @param params String
     * @return ResultSet
     */
    public static ResultSet sqlSelectCount(Connection connection, String table,
                                      ArrayList<String> columns, String params){

        //Faccio una chiamata al db
        Statement statement;
        String query;

        //Build the query
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT COUNT(");
        if(columns != null) {
            for (int i = 0; i < columns.size(); i++) {
                builder.append(columns.get(i));
                if (i != columns.size() - 1)
                    builder.append(",");
            }
        }else {
            builder.append("*");
        }
        builder.append(") AS total FROM ");
        builder.append(table);
        builder.append(" WHERE ");
        builder.append(params);
        builder.append(";");
        query = builder.toString();

        try{
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            connection.close();
            if (resultSet != null){
                return resultSet;
            }else {
                return null;
            }
        }catch (Exception sqle){
            System.out.println(sqle.toString());
            sqle.printStackTrace();
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /**
     *
     * @param connection Connection
     * @param query Stirng
     */
    public static void sqlUpdate(Connection connection, String query){
        executeQuery(connection, query);
    }

    /**
     *
     * @param connection Connection
     * @param query String
     * @return boolean
     */
    public static boolean executeQuery(Connection connection, String query){
        Statement stmt;
        try {
            stmt = connection.createStatement();
            stmt.executeQuery(query);
            stmt.close();
            connection.close();
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return false;
        }
    }

    /**
     * Metodo per la connessione al database locale Heroku
     * @return Connection
     */
    public static Connection getConnectionHeroku(){
        try {
            URI dbUri;
            dbUri = new URI(System.getenv("DATABASE_URL"));

            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

            return DriverManager.getConnection(dbUrl, username, password);

        } catch (URISyntaxException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
