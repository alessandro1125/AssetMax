package com.altervista.org;

import com.assetx.libraries.utils.http.HttpRequestInstance;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@WebServlet(
        name = "DownloadZip",
        urlPatterns = "/download_zip/*"
)
public class DownloadAssetMax extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        try {
            FileInputStream fileInputStream = new FileInputStream(getClass().getResource("/AssetMax.zip").getFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
