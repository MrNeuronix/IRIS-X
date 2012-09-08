package ru.phsystems.irisx;
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
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;

public class Web implements Runnable {

    Thread t;
    Long time_dalay;

    public Web() {
        t = new Thread(this);
        t.start();
    }

    @Override
    public synchronized void run() {

        System.out.println("[web] Thread started!");

        // Jetty

        try {
            Server server = new Server(8080);

            // Тут определяется контекст для контроллера
            ServletContextHandler context0 = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context0.setContextPath("/control");
            context0.addServlet(new ServletHolder(new ControlHandler()),"/*");
            context0.addServlet(new ServletHolder(new ControlHandler("Buongiorno Mondo")),"/it/*");
            context0.addServlet(new ServletHolder(new ControlHandler("Bonjour le Monde")),"/fr/*");

            // Тут определяется контекст для статики (html, cs, картинки и т.д.)
            ResourceHandler resource_handler = new ResourceHandler();
            resource_handler.setDirectoriesListed(true);
            resource_handler.setWelcomeFiles(new String[]{ "index.html" });
            ContextHandler context = new ContextHandler();
            context.setContextPath("/");
            context.setResourceBase("./www/");
            context.setClassLoader(Thread.currentThread().getContextClassLoader());
            context.setHandler(resource_handler);

            // Выставляем контексты в коллекцию
            ContextHandlerCollection contexts = new ContextHandlerCollection();
            contexts.setHandlers(new Handler[] { context0, context });

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
