<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.net.URISyntaxException" %>
<%@ page import="java.net.URI" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.net.URL" %>
<html>
    <head>
        <title>Get Advertisment</title>
    </head>
    <body>

        <%


            //createTable();

            //Reindirizzo al login
            //createTable();
            /*String redirectURL = "/login";
            response.sendRedirect(redirectURL);*/
            StringBuffer protocol = request.getRequestURL();
            String stringa = new URL(protocol.toString()).getProtocol();
        %>
        <p><%= protocol%></p>
        <%!

            /**
             *
             * @return Connection
             */
            private static Connection getConnection(){

                Connection connection = null;
                try {

                    try {

                        Class.forName("org.postgresql.Driver");

                    } catch (ClassNotFoundException e) {

                        System.out.println("Where is your PostgreSQL JDBC Driver? "
                                + "Include in your library path!");
                        e.printStackTrace();
                        return null;

                    }

                    String url = "jdbc:postgresql://ec2-79-125-110-209.eu-west-1.compute.amazonaws.com:5432/" +
                            "d2qht4msggj59q?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory&" +
                            "sslmode=require&user=sagdjsuxgvztxk&" +
                            "password=8be153a38455d94b7422704cec7de29ab6b0772c07f40a94f71932387641710a";

                    connection = DriverManager.getConnection(url);

                }
                catch (Exception e) {
                    System.err.println("Database connection failed");
                    System.err.println(e.getMessage());
                }

                return connection;

            }

        %>

        Index jsp 2 <br>

    </body>
</html>
