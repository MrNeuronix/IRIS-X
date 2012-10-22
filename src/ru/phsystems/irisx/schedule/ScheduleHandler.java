package ru.phsystems.irisx.schedule;
/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 22.10.12
 * Time: 14:10
 * License: GPL v3
 */

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j

// Класс отвечает за выставление значений для задач планировщика

public class ScheduleHandler extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            if (request.getParameter("id").isEmpty()) {
                Task task = new Task();

                task.setDate(task.setDateAsString(request.getParameter("date")));
                task.setEclass(request.getParameter("class"));
                task.setCommand(request.getParameter("command"));
                task.setType(Integer.valueOf(request.getParameter("command")));
                task.setValidto(task.setDateAsString(request.getParameter("validto")));
                task.setInterval(request.getParameter("interval"));
                task.setEnabled(1);

                if (task.save()) {
                    log.info("[scheduler] Task [" + task.getId() + "] successfully saved");
                } else {
                    log.info("[scheduler] Error in save task [" + task.getId() + "]!");
                }

            } else {
                Task task = new Task(Integer.valueOf(request.getParameter("id")));

                task.setDate(task.setDateAsString(request.getParameter("date")));
                task.setEclass(request.getParameter("class"));
                task.setCommand(request.getParameter("command"));
                task.setType(Integer.valueOf(request.getParameter("command")));
                task.setValidto(task.setDateAsString(request.getParameter("validto")));
                task.setInterval(request.getParameter("interval"));
                task.setEnabled(Integer.valueOf(request.getParameter("enabled")));

                if (task.save()) {
                    log.info("[scheduler] New task [" + task.getId() + "] successfully saved");
                } else {
                    log.info("[scheduler] Error in save new task [" + task.getId() + "]!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        response.getWriter().println("done");
    }
}
