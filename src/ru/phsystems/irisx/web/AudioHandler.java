package ru.phsystems.irisx.web;

/**
 * Created with IntelliJ IDEA.
 * Author: Nikolay A. Viguro
 * Date: 09.09.12
 * Time: 17:45
 * License: GPL v3
 *
 *    Этот класс является proxy для mjpeg потока с камер
 *
 */

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class AudioHandler extends HttpServlet {


    public AudioHandler() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // правильный boundary - самое главное
        response.setContentType("audio/wav");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Cache-Control", "no-cache");

        //////////////////////////////////////

        InputStream in = null;
        OutputStream out = null;

        try {

            System.err.println("[audio] Get stream!");

            // URL = http://localhost:8080/control/audio?cam=10
            URL cam = new URL("http://192.168.10." + request.getParameter("cam") + "/audio.cgi");
            URLConnection uc = cam.openConnection();
            out = new BufferedOutputStream(response.getOutputStream());

            in = uc.getInputStream();
            byte[] bytes = new byte[8192];
            int bytesRead;

            while ((bytesRead = in.read(bytes)) != -1) {
                out.write(bytes, 0, bytesRead);
            }

        } catch (IOException ex) {
            // Disconnect detected
            System.err.println("[audio " + request.getParameter("cam") + "] Audio client disconnected");
            // Прерываем поток, иначе передача не будет остановена
            Thread.currentThread().interrupt();
        }
    }
}







