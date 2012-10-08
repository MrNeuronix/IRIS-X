package ru.phsystems.irisx.web;
/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 08.09.12
 * Time: 18:36
 * License: GPL v3
 */

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

// Парсинг страниц

public class HTMLHandler extends HttpServlet {

    private static Logger log = Logger.getLogger(HTMLHandler.class.getName());

    public HTMLHandler() throws IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();

        if (path.equals("/")) {
            path = "/index.html";
        }

        try {
            /*  first, get and initialize an engine  */
            VelocityEngine ve = new VelocityEngine();
            ve.init();

            /*  next, get the Template  */
            Template t = ve.getTemplate("./templates" + path, "UTF-8");

            /*  create a context and add data */
            VelocityContext context = new VelocityContext();

            // Данные для страниц - в шаблонизатор
            String fileName = path.substring(path.lastIndexOf('/') + 1, path.length());
            String fileNameWithoutExtn = fileName.substring(0, fileName.lastIndexOf('.'));

            PagesContext pc = new PagesContext();
            HashMap pagesKeysValues = pc.getContext(fileNameWithoutExtn);

            Iterator<Map.Entry<Object, Object>> iterPages = pagesKeysValues.entrySet().iterator();
            while (iterPages.hasNext()) {
                Map.Entry<Object, Object> entry = iterPages.next();
                context.put(entry.getKey().toString(), entry.getValue());
            }

            /* now render the template into a StringWriter */
            StringWriter writer = new StringWriter();
            t.merge(context, writer);

            /* show the World */
            response.getWriter().println(writer.toString());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            log.info("[web] Error in generation HTMLHandler: " + path);
            //e.printStackTrace();
        }
    }
}