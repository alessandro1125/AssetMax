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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws NullPointerException, IOException {

        try {
            OutputStream outputStream = response.getOutputStream();
            //FileInputStream fileInputStream = new FileInputStream(getClass().getResource("/build").getFile());


            System.out.println("POrco dio");
            System.out.println("PAtH: " + );


            String path = OTAHttpHelper.class
                    .getClassLoader().getResource("AssetMax.zip").toString();

            File folder = new File("../"+path);

            File pathList[] = folder.listFiles();
            assert pathList != null;
            System.out.println("PAth 1 : " + pathList[0].getPath());


            /*while (pathList != null){

                pathList
            }*/



            /*
            System.out.println("Path: " + getClass().getResource("AssetMax.zip").getPath());
            File[] listOfFiles = folder.listFiles();
            BufferedReader bufferReader = null;

            assert listOfFiles != null;
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    System.out.println(file.getPath());
                    try{
                        InputStream inputStream = new FileInputStream(file.getPath());
                        InputStreamReader streamReader = new InputStreamReader(inputStream);
                        //Instantiate the BufferedReader Class
                        bufferReader = new BufferedReader(streamReader);
                        //Variable to hold the each line data
                        String line;
                        // Read file line by line...
                        while ((line = bufferReader.readLine()) != null)   {
                            System.out.println(line);
                            outputStream.write(line.getBytes());
                            line = line.trim();
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }finally{
                        bufferReader.close();
                    }
                }
            }*/


            /*int bytes = src.read();
            while (bytes != -1){
                outputStream.write(bytes);
                bytes = fileInputStream.read();
            }*/

            response.flushBuffer();
            //fileInputStream.close();
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
