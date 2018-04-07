package com.altervista.org;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "ActionServlet",
        urlPatterns = {"/operations/*"}
)
public class DoOperations extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){
        try {
            RequestDispatcher view = req.getRequestDispatcher("/index_exemple.jsp");
            view.forward(req,resp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
