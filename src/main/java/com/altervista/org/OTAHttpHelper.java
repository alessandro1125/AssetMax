package com.altervista.org;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(
        name = "OTAHttpHelper",
        urlPatterns = {"/ota_helper/*"}
)
public class OTAHttpHelper extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            OutputStream outputStream = response.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(getClass().getResource("/build").getFile());

            /*
            File src = new File(getClass().getResource("/build").toString());
            String srcList[] = src.list();
            assert srcList != null;
            for (String dir : srcList) {

            }
            */

            int bytes = fileInputStream.read();
            while (bytes != -1){
                outputStream.write(bytes);
                bytes = fileInputStream.read();
            }

            response.flushBuffer();
            fileInputStream.close();
            outputStream.close();

            /*
            String filename = "AssetMax.zip";

            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\"");

            int bytes;
            while ((bytes = fileInputStream.read()) != -1) {
                System.out.println(bytes);
                outputStream.write(bytes);
            }
            fileInputStream.close();
            response.flushBuffer();
            */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
