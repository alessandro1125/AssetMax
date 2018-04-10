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
     * @param tablename String
     * @param columns HashMap<Stirng col_name, Stirng type>
     * @return boolean
     */
    public static boolean sqlCreateTable(Connection connection, String tablename, HashMap<String, String> columns){
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("CREATE TABLE ").append(tablename).append("(");
        int count = 0;
        for (String key : columns.keySet()){
            count++;
            queryBuilder.append(key).append(" ").append(columns.get(key));
            if(count != columns.size())
                queryBuilder.append(",");
        }
        queryBuilder.append(");");
        return executeQuery(connection, queryBuilder.toString());
    }

    public static boolean sqlAdd(Connection connection, HashMap<String, String> record, String table){
        String keys, values;
        StringBuilder keysBuilder, valuesBuilder;
        keysBuilder = new StringBuilder();
        valuesBuilder = new StringBuilder();
        int count = 0;
        for (String key : record.keySet()){
            count++;
            keysBuilder.append(key);
            valuesBuilder.append("'");
            valuesBuilder.append(record.get(key));
            valuesBuilder.append("'");
            if (count != record.size()) {
                valuesBuilder.append(" ,");
                keysBuilder.append(" ,");
            }
        }
        keys = keysBuilder.toString();
        values = valuesBuilder.toString();

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
    private static boolean executeQuery(Connection connection, String query){
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
