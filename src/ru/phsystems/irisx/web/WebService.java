package ru.phsystems.irisx.web;
/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 08.09.12
 * Time: 15:45
 * License: GPL v3
 */

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class WebService implements Runnable {

    public WebService() {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public synchronized void run() {

        System.out.println("[web] Service started");

        // Jetty

        try {
            Server server = new Server(16101);

            // Тут определяется контекст для контроллера
            ServletContextHandler context0 = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context0.setContextPath("/control");
            context0.addServlet(new ServletHolder(new ControlHandler()), "/*");
            context0.addServlet(new ServletHolder(new VideoHandler()), "/video/*");

            ServletContextHandler context1 = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context1.setContextPath("/");
            context1.addServlet(new ServletHolder(new HTMLHandler()), "/*");

            // Тут определяется контекст для статики (html, cs, картинки и т.д.)
            ResourceHandler resource_handler = new ResourceHandler();
            ContextHandler context = new ContextHandler();
            context.setContextPath("/static");
            context.setResourceBase("./www/");
            context.setClassLoader(Thread.currentThread().getContextClassLoader());
            context.setHandler(resource_handler);

            // Выставляем контексты в коллекцию
            ContextHandlerCollection contexts = new ContextHandlerCollection();
            contexts.setHandlers(new Handler[]{context0, context1, context});

            // Назначаем коллекцию контекстов серверу и запускаем его
            server.setHandler(contexts);
            server.start();
            server.join();

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
