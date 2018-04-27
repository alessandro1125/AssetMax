package com.altervista.org.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.google.common.net.HttpHeaders.X_FORWARDED_PROTO;

/**
 * Utils class for AssetMax project
 * @author Alessandro Giordano
 */
public class Utils {

    /**
     * This method redirect to HTTPS protocol from HTTP protocol
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     */
    public static void checkForHttpsProtocol(HttpServletRequest request, HttpServletResponse response){
        if (request.getHeader(X_FORWARDED_PROTO) != null) {
            if (request.getHeader(X_FORWARDED_PROTO).indexOf("https") != 0) {
                try {
                    response.sendRedirect("https://assetmax.herokuapp.com");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
