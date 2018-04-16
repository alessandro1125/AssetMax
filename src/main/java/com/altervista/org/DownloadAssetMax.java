package com.altervista.org;

import com.assetx.libraries.utils.http.HttpRequestInstance;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(
        name = "DownloadZip",
        urlPatterns = "/download_zip/*"
)
public class DownloadAssetMax extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            OutputStream outputStream = response.getOutputStream();
            FileInputStream fileInputStream = new FileInputStream(getClass().getResource("/AssetMax.zip").getFile());
            byte[] buffer = new byte[4096];
            int length;
            while ((length = fileInputStream.read(buffer)) > 0){
                outputStream.write(buffer, 0, length);
            }
            fileInputStream.close();
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
