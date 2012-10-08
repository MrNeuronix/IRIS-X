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

import ru.phsystems.irisx.utils.MjpegFrame;
import ru.phsystems.irisx.utils.MjpegInputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

public class VideoHandler extends HttpServlet {

    private static Logger log = Logger.getLogger(VideoHandler.class.getName());

    public VideoHandler() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // правильный boundary - самое главное
        response.setContentType("multipart/x-mixed-replace; boundary=--video boundary--");
        response.setStatus(HttpServletResponse.SC_OK);

        //////////////////////////////////////

        log.info("[cam " + request.getParameter("cam") + "] Client connected");

        OutputStream output = response.getOutputStream();

        // URL = http://host/control/video?cam=<number>
        URL camURL = new URL("http://192.168.10." + request.getParameter("cam") + "/video.cgi");
        URLConnection uc = camURL.openConnection();

        MjpegFrame frame = null;
        MjpegInputStream inMJPEG = new MjpegInputStream(new BufferedInputStream(uc.getInputStream()));

        while ((frame = inMJPEG.readMjpegFrame()) != null) {

            try {
                // Определяем, есть ли лицо в фрейме
                //byte[] faceImg = face.detect(frame.getJpegBytes());

                // Заменяем фрейм новым изображением
                //frame.setJpegBytes(faceImg);

                output.write(frame.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}







