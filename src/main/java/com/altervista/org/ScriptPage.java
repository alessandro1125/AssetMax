package com.altervista.org;

import com.assetx.libraries.utils.SqlUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

@WebServlet(
        name = "ScriptPageServlet",
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
            assert out != null;
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

            //Trovo l'array di versioni valide
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
                    String accountId = null;
                    String accountName = null;
                    Calendar calendar = new GregorianCalendar();
                    String accessTime = calendar.getTime().toString();

                    //Ricavo i parametri da json
                    StringBuilder stringBuilder = new StringBuilder();
                    BufferedReader bufferedReader = null;
                    try {
                        InputStream inputStream = request.getInputStream();
                        if (inputStream != null) {
                            bufferedReader = new BufferedReader(new InputStreamReader(
                                    inputStream));
                            char[] charBuffer = new char[128];
                            int bytesRead = -1;
                            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                                stringBuilder.append(charBuffer, 0, bytesRead);
                            }
                        } else {
                            stringBuilder.append("Cazzo");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    String body = stringBuilder.toString();
                    try {
                        out.write(body.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Decodifico il body in JSON
                    try {
                        JSONObject inputJson = new JSONObject(body);
                        accountId = inputJson.getString("id_account");
                        accountName = inputJson.getString("name_account");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (accountId == null || accountName == null){
                        try {
                            out.write("Errore di sistema: parametri nulli".getBytes());
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }

                    //rimuovo il caratterre 0 dal time
                    assert accessTime != null;
                    byte[] timeBytes = accessTime.getBytes();
                    ArrayList<Byte> byteTime = new ArrayList<>();
                    for (Byte bytes : timeBytes){
                        if (bytes != 0){
                            byteTime.add(bytes);
                        }
                    }
                    byte[] tmp = new byte[byteTime.size()];
                    for (int i = 0; i < byteTime.size(); i++){
                        tmp[i] = byteTime.get(i);
                    }

                    accessTime = new String(tmp);

                    //Controllo la corrispondenza
                    ResultSet resultCount = SqlUtils.sqlSelectCount(SqlUtils.getConnectionHeroku(), "assetmaxusers",
                            null,"account_id='" + accountId + "'");
                    try {
                        assert resultCount != null;
                        resultCount.next();
                        if(resultCount.getInt("total") != 1){
                            try {
                                out.write("0".getBytes());
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }else {
                            try {
                                //Vedo se è stato attivato
                                ResultSet result = SqlUtils.sqlSelect(SqlUtils.getConnectionHeroku(), "assetmaxusers",
                                        null,"account_id='" + accountId + "'");
                                assert result != null;
                                result.next();
                                if (result.getString("active").equals("1")){
                                    //Autenticato
                                    try {
                                        out.write("1".getBytes());

                                        //Registro l'accesso
                                        //Controllo se è già presente un record per questo utente
                                        ResultSet accesResult = SqlUtils.sqlSelectCount(SqlUtils.getConnectionHeroku(),
                                                "assetmaxuseractives", null, "account_id='" +
                                                accountId + "'");
                                        assert accesResult != null;
                                        accesResult.next();
                                        if (accesResult.getInt("total") != 1){
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

                                            HashMap<String, String> map = new HashMap<>();
                                            map.put("account_id", accountId);
                                            map.put("last_access", accessTime);
                                            map.put("account_name", accountName);
                                            HashMap<String, String> params = new HashMap<>();
                                            map.put("account_id", accountId);
                                            SqlUtils.sqlUpdate(SqlUtils.getConnectionHeroku(),
                                                    "assetmaxuseractives", map, params);
                                        }
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                }else {
                                    try {
                                        out.write("0".getBytes());
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
                    out.write("Versione Obsoleta".getBytes());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            try {
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
