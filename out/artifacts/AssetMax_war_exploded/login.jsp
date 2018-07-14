<%@ page import="java.net.URISyntaxException" %>
<%@ page import="java.net.URI" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Base64" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.util.GregorianCalendar" %>
<%@ page import="com.assetx.libraries.utils.SqlUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="it" dir="ltr">
    <head>
        <title>Asset Max</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="/styles" type="text/css">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3mobile.css">
    </head>
    <body class="form-style-8">

        <%

            //Controllo l'action
            int action = 0;
            String message, base64Message;

            try {
                if(request.getParameter("message") != null){
                    try{
                        base64Message = request.getParameter("message");
                        message = new String(Base64.getDecoder().decode(base64Message));
                        //Stampo il message
                        %>
                        <p class="form-style-8"><%= message %></p>
                        <%

                        if (base64Message.equals("VXNlciBkb2Vzbid0IGV4aXN0")){//Login non corretto
                            //Cancello i cookie
                            Cookie[] cookies = request.getCookies();
                            if (cookies != null) {

                                for (int i = 0; i < cookies.length; i++) {

                                    Cookie cookie = cookies[i];
                                    cookies[i].setValue(null);
                                    cookies[i].setMaxAge(0);
                                    response.addCookie(cookie);
                                }
                            }
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            try{
                if(request.getParameter("action") != null){
                    try{
                        action = Integer.parseInt(request.getParameter("action"));
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            }catch (Exception e){
                action = 0;
            }


            switch (action){
                case 0:

                    //Controllo i coockies per il login
                    try{
                        Cookie[] cookies = request.getCookies();
                        if (cookies != null) {
                            String email = null;
                            String password = null;

                            for (Cookie cookie : cookies) {
                                try {
                                    if (cookie.getName().equals("email"))
                                        email = cookie.getValue();
                                    if (cookie.getName().equals("password"))
                                        password = cookie.getValue();
                                }catch (NullPointerException e){
                                    e.printStackTrace();
                                }
                            }
                            if (email != null && password != null){
                                //Faccio il login
                                authenticationParser(request, response, email, password, action);
                            }
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }

                    //Mostro il form per il login

                    %>


        <br>
        <div dir="ltr" style="text-align: center;background-color:white;font-family:sans-serif;font-weight:lighter;color:#595959;">
            <div class="form-style-8">
                <h2>Login</h2>
                <form action="login?action=1" method="post" enctype="application/x-www-form-urlencoded">
                    <input type="email" name="email" placeholder="Your email..."/>
                    <input type="password" name="password" placeholder="Your password..."/>
                    <input type="submit" value="Login">
                </form>
                <form action=sign_in?action=0" method="post" enctype="application/x-www-form-urlencoded">
                    <input type="submit" value="Sign In">
                </form>
                <form action="sign_in?action=3" method="post" enctype="application/x-www-form-urlencoded">
                    <input type="submit" value="Reset Password">
                </form>
            </div>
        </div>
        <div class = "form-style-8" style="bottom:0;left:20%;">
            Contacts:<blockquote> urimkuci.assetx@gmail.com<br>alessandrogiordano.assetx@gmail.com</blockquote>
        </div>

                    <%
                    break;
                case 1:

                    //FACCIO IL LOGIN

                    String email = null;
                    String password = null;

                    try{
                        email = request.getParameter("email");
                        password = request.getParameter("password");
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    authenticationParser(request, response, email, password, action);
                break;
            }



        %>


        <%!

            /**
             *
             * @param request HttpServletRequest
             * @param response HttpServletResponse
             * @param email String
             * @param action int
             * @param password String
             */
            private static void authenticationParser(HttpServletRequest request, HttpServletResponse response,
                                                     String email, String password, int action){

                try {
                    //Mi connetto al db
                    Connection connection;
                    connection = SqlUtils.getConnectionHeroku();

                    if (connection != null) {
                        if (email != null && password != null) {

                            //Cerco la corrispondenza nella tabella users

                            switch (authenticateUser(connection, email, password)) {
                                case 0:
                                    //Login succesfully done

                                    //Salvo i cookie se action = 1
                                    if (action == 1) {
                                        try {
                                            Cookie emailCk = new Cookie("email", email);
                                            emailCk.setMaxAge(60 * 60 * 24 * 360);
                                            Cookie passwordCk = new Cookie("password", password);
                                            passwordCk.setMaxAge(60 * 60 * 24 * 360);

                                            response.addCookie(emailCk);
                                            response.addCookie(passwordCk);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    //Se sono autenticato
                                    String dispUrl = null;
                                    try {
                                        dispUrl = request.getParameter("from_page");
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    if (dispUrl == null)
                                        dispUrl = "handle_account.jsp";
                                    //Controllo dove devo essere reindirizzato
                                    request.setAttribute("email", email);
                                    request.setAttribute("password", password);
                                    request.setAttribute("authorization", "authorized");
                                    RequestDispatcher dispatcher;
                                    dispatcher = request.getRequestDispatcher(dispUrl);
                                    dispatcher.forward(request, response);
                                    break;

                                case 1:
                                    //Email Wrong
                                    System.out.println("User email not found");
                                    //Invio un messaggio all'utente
                                    errorOccurred(response, "User doesn't exist");
                                    break;

                                case 2:
                                    //Password wrong
                                    System.out.println("User password not correct");
                                    //Invio un messaggio all'utente
                                    errorOccurred(response, "Password wrong");
                                    break;

                                case 3:
                                    //Non attivo
                                    System.out.println("L'utente non è attivo");
                                    //Invio un messaggio all'utente
                                    errorOccurred(response, "User non activated");
                                    break;

                                default:
                                    break;

                            }

                        } else {
                            //Se uno e entrambi i cambi sono nulli
                            System.out.println("Parameters are not valid");
                            //Invio un messaggio all'utente
                            errorOccurred(response, "Enter valids parameters");
                        }

                        try {
                            connection.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    } else {
                        //Se fallisce la connessione al database
                        System.out.println("Unable to connect to database");
                        //Invio un messaggio all'utente
                        errorOccurred(response, "An error has occurred");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            /**
             *
             * @param httpSerletResponse HttpServletResponse
             * @param message String
             */
            private static void errorOccurred(HttpServletResponse httpSerletResponse, String message){
                byte[] messageBy = Base64.getEncoder().encode(message.getBytes());
                String redirectURL = "login?action=0&message=" + new String(messageBy);
                try {
                    httpSerletResponse.sendRedirect(redirectURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            /**
             *
             * @param connection Connection
             * @param email String
             * @param password String
             * @return int
             */
            private static int authenticateUser(Connection connection, String email, String password){

                //Faccio una chiamata al db
                Statement statement;
                String query;

                query = "SELECT email,password,attivo FROM assetmaxusers";

                try{
                    statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    boolean emailFounded = false;
                    boolean passwordFounded = false;
                    boolean attivato = false;
                    while (resultSet.next()){
                        //Controllo corrispondenze
                        if (resultSet.getString("email").equals(email))
                            emailFounded = true;
                        if (emailFounded && resultSet.getString("password").equals(password))
                            passwordFounded = true;
                        if (emailFounded && passwordFounded && resultSet.getString("attivo").equals("1"))
                            attivato = true;
                    }
                    connection.close();
                    //Genero output
                    if (!emailFounded)
                        return 1;
                    if (!passwordFounded)
                        return 2;
                    if (!attivato)
                        return 3;
                    return 0;


                }catch (SQLException sqle){
                    sqle.printStackTrace();
                    return -1;
                }
            }
        %>
    </body>
</html>
