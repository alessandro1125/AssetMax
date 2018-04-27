package com.altervista.org;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.google.common.net.HttpHeaders.X_FORWARDED_PROTO;

@WebServlet(
        name = "ServletStylesManager",
        urlPatterns = "/styles/*"
)
public class StylesManager extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        //Controllo se il protocollo è https
        if (request.getHeader(X_FORWARDED_PROTO) != null) {
            if (request.getHeader(X_FORWARDED_PROTO).indexOf("https") != 0) {
                try {
                    response.sendRedirect("https://assetmax.herokuapp.com");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //Reindirizzo all'unica page .css
        try {
            OutputStream outputStream = response.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(getClass().getResource("/stile-1.css").getFile());

            int bytes;
            while ((bytes = fileInputStream.read()) != -1) {
                outputStream.write(bytes);
            }
            outputStream.flush();
            outputStream.close();
            fileInputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        doGet(request, response);
    }

}
