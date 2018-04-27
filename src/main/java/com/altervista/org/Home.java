package com.altervista.org;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.google.common.net.HttpHeaders.X_FORWARDED_PROTO;

@WebServlet(
        name = "HomeServlet",
        urlPatterns = {"/"}
)
public class Home extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){

        //Cotnrollo se il protocollo è https
        if (req.getHeader(X_FORWARDED_PROTO) != null) {
            if (req.getHeader(X_FORWARDED_PROTO).indexOf("https") != 0) {
                try {
                    resp.sendRedirect("https://" + req.getServerName() + (req.getPathInfo() == null ? "" : req.getPathInfo()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        /*
        try {
            RequestDispatcher view;
            view = req.getRequestDispatcher("/login");
            view.forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        doGet(req, resp);
    }
}
