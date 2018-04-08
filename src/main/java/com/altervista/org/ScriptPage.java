package com.altervista.org;

import utils.SqlUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

@WebServlet(
        name = "LoginServlet",
        urlPatterns = {"/script_page/*"}
)
public class ScriptPage extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){

        //Ricavo i parametri principali
        String action = null;
        String version = null;
        try {
            action = request.getParameter("action");
            version = request.getParameter("version");
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        if(action != null && version != null){

            //Get the output stream
            ServletOutputStream out = null;
            try {
                out = response.getOutputStream();
            }catch (IOException e){
                e.printStackTrace();
            }

            //Controllo se la versione è corretta
            boolean versionControl = false;
            String versionString = null;
            ResultSet resultSet = SqlUtils.sqlSelect(SqlUtils.getConnectionHeroku(), "assetmaxtools"
                        ,null,"id=1");
            try {
                assert resultSet != null;
                resultSet.next();
                versionString = resultSet.getString("valore");
            }catch (SQLException e){
                e.printStackTrace();
            }

            String[] versions = null;
            try {
                assert versionString != null;
                versions = versionString.split(",");
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            assert versions != null;
            for (String ver : versions){
                if (version.equals(ver))
                    versionControl = true;
            }

            if (versionControl){
                //Se la versione è corretta
                //Controllo l'azione da fare
                if (action.equals("check_user")){
                    //Controllo se l'utente è attivato
                    String accountId = request.getParameter("id_account");
                    String accountName = request.getParameter("name_account");
                    String accessTime = request.getParameter("current_time");

                    //Controllo la corrispondenza
                    ResultSet result = SqlUtils.sqlSelect(SqlUtils.getConnectionHeroku(), "assetmaxusers",
                            null,"account_id='" + accountId + "'");
                    try {
                        assert result != null;
                        if(result.getString("total").equals("1")){
                            try {
                                assert out != null;
                                out.write("0".getBytes());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }else {
                            try {
                                result.next();
                                if (result.getString("active").equals("1")){
                                    //Autenticato
                                    try {
                                        assert out != null;
                                        out.write("1".getBytes());

                                        //Registro l'accesso
                                        //Controllo se è già presente un record per questo utente
                                        ResultSet accesResult = SqlUtils.sqlSelect(SqlUtils.getConnectionHeroku(),
                                                "assetmaxuseractives", null, "account_id='" +
                                                accountId + "'");
                                        assert accesResult != null;
                                        if (accesResult.getString("total").equals("1")){
                                            //Creo un nuovo record
                                            HashMap<String, String> records = new HashMap<>();
                                            records.put("account_id", accountId);
                                            records.put("account_name", accountName);
                                            records.put("last_access", accessTime);
                                            SqlUtils.sqlAdd(SqlUtils.getConnectionHeroku(), records, "assetmaxuseractives");
                                        }else {
                                            //Aggiorno l'esistente
                                            String query = "UPDATE assetmaxuseractives SET account_id='" + accountId +
                                                    "', account_name='" + accountName + "', last_access='" + accessTime +
                                                    "' WHERE account_id='" + accountId + "';";
                                            SqlUtils.sqlUpdate(SqlUtils.getConnectionHeroku(), query);
                                        }
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                //se la versione non è corretta
                try {
                    assert out != null;
                    out.write("Versione Obsoleta".getBytes());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            try {
                assert out != null;
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            String redirectURL = "/home";
            try {
                response.sendRedirect(redirectURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        doGet(req,resp);
    }
}
