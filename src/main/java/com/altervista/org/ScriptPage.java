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
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        

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
            //Inizializzo il JSON di risposta
            JSONObject outputObject = new JSONObject();

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
                    String accountId = request.getParameter("account_id");
                    String accountName = removeZeroChar(request.getParameter("account_name"));
                    String accountBalance = removeZeroChar(request.getParameter("account_balance"));
                    String accountCredit = removeZeroChar(request.getParameter("account_credit"));
                    String accountCompany = removeZeroChar(request.getParameter("account_company"));
                    String accountCurrency = removeZeroChar(request.getParameter("account_currency"));
                    String accountEquity = removeZeroChar(request.getParameter("account_equity"));
                    String accountFreeMargin = removeZeroChar(request.getParameter("account_freemargin"));
                    String accountLeverage = removeZeroChar(request.getParameter("account_leverage"));
                    String accountMargin = removeZeroChar(request.getParameter("account_margin"));
                    String accountServer = removeZeroChar(request.getParameter("account_server"));
                    String runningState = removeZeroChar(request.getParameter("running_state"));
                    Calendar calendar = new GregorianCalendar();
                    String accessTime = removeZeroChar(calendar.getTime().toString());

                    if (accountId == null){
                        try {
                            outputObject.put("response", "Errore di sistema: parametri nulli");
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }else
                        accountId = removeZeroChar(accountId);

                    //Controllo la corrispondenza
                    ResultSet resultCount = SqlUtils.sqlSelectCount(SqlUtils.getConnectionHeroku(), "assetmaxusers",
                            null,"account_id='" + accountId + "'");
                    try {
                        assert resultCount != null;
                        resultCount.next();
                        if(resultCount.getInt("total") != 1){
                            try {
                                outputObject.put("response", "0");
                            }catch (JSONException e){
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
                                        outputObject.put("response", "1");

                                        //Registro l'accesso
                                        //Controllo se è già presente un record per questo utente
                                        ResultSet accesResult = SqlUtils.sqlSelectCount(SqlUtils.getConnectionHeroku(),
                                                "assetmaxuseractives", null, "account_id='" +
                                                accountId + "'");
                                        assert accesResult != null;
                                        accesResult.next();
                                        HashMap<String, String> records = new HashMap<>();
                                        records.put("account_id", accountId);
                                        records.put("account_name", accountName);
                                        records.put("last_access", accessTime);
                                        records.put("account_balance", accountBalance);
                                        records.put("account_credit", accountCredit);
                                        records.put("account_company",accountCompany);
                                        records.put("account_currency", accountCurrency);
                                        records.put("account_equity", accountEquity);
                                        records.put("account_freemargin", accountFreeMargin);
                                        records.put("account_leverage", accountLeverage);
                                        records.put("account_margin", accountMargin);
                                        records.put("account_server", accountServer);
                                        records.put("running_state", runningState);
                                        if (accesResult.getInt("total") != 1){
                                            //Creo un nuovo record
                                            SqlUtils.sqlAdd(SqlUtils.getConnectionHeroku(), records, "assetmaxuseractives");
                                        }else {
                                            //Aggiorno l'esistente
                                            HashMap<String, String> params = new HashMap<>();
                                            params.put("account_id", accountId);
                                            SqlUtils.sqlUpdate(SqlUtils.getConnectionHeroku(),
                                                    "assetmaxuseractives", records, params);
                                        }
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                }else {
                                    try {
                                        outputObject.put("response", "0");
                                    }catch (JSONException e){
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
                    outputObject.put("response", "2");
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            try {
                out.write(outputObject.toString().getBytes());
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

    private String removeZeroChar(String input){
        byte[] timeBytes = input.getBytes();
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

        return new String(tmp);
    }
}
