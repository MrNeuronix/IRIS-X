package ru.phsystems.irisx;
/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 08.09.12
 * Time: 18:36
 * License: GPL v3
 */

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

// Парсинг страниц

public class HTMLHandler extends HttpServlet
{
    public HTMLHandler(){}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();

        if(path.equals("/")) {
            path = "/index.html";
        }

        /*  first, get and initialize an engine  */
        VelocityEngine ve = new VelocityEngine();
        ve.init();

        /*  next, get the Template  */
        Template t = ve.getTemplate( "./templates"+path, "UTF-8");

        /*  create a context and add data */
        VelocityContext context = new VelocityContext();
        context.put("name", "World");

        /* now render the template into a StringWriter */
        StringWriter writer = new StringWriter();
        t.merge( context, writer );

        /* show the World */
        response.getWriter().println( writer.toString() );
    }
}