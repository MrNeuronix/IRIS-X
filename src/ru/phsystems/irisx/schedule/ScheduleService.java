package ru.phsystems.irisx.schedule;

/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 10.09.12
 * Time: 13:26
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.phsystems.irisx.Iris;
import ru.phsystems.irisx.utils.Module;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScheduleService
        implements Runnable {
    Thread t = null;
    private static Logger log = LoggerFactory.getLogger(ScheduleService.class.getName());

    public ScheduleService() {
        this.t = new Thread(this);
        this.t.start();
    }

    public Thread getThread() {
        return this.t;
    }

    public synchronized void run() {
        log.info("[schedule] Service starting");

        // Актуализируем даты запуска всех тасков

        try {

            ResultSet rsActualize = Iris.sql.select("SELECT * FROM scheduler WHERE enabled='1' AND date < NOW()");
            Date nowActualize = new Date();

            while (rsActualize.next()) {
                Integer id = Integer.valueOf(rsActualize.getInt("id"));
                String interval = rsActualize.getString("interval");
                Integer type = rsActualize.getInt("type");
                Date validto = rsActualize.getDate("validto");

                CronExpression cron = new CronExpression(interval);
                Date nextRunDate = cron.getNextValidTimeAfter(nowActualize);
                String nextRun = cron.getNextRun(nextRunDate);

                if (type.intValue() == 1) {
                    log.info("Actualize task time! Next run at [" + nextRun + "]");
                    Iris.sql.doQuery("UPDATE scheduler SET date='" + nextRun + "' WHERE id='" + id + "'");
                } else if (type.intValue() == 3) {

                    if (validto.before(nextRunDate)) {
                        log.info("Actualize task time! Set task to disable");
                        Iris.sql.doQuery("UPDATE scheduler SET enabled='0' WHERE id='" + id + "'");
                    } else {
                        log.info("Actualize task time! Next run at [" + nextRun + "]");
                        Iris.sql.doQuery("UPDATE scheduler SET date='" + nextRun + "' WHERE id='" + id + "'");
                    }
                } else {
                    log.info("Actualize task time! Skip task");
                }

            }

            rsActualize.close();

        } catch (Exception e) {
            log.info("Error at actualizing tasks!");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // Запускаем выполнение тасков

        while (true) {
            try {
                ResultSet rs = Iris.sql.select("SELECT * FROM scheduler WHERE enabled='1'");
                Date now = new Date();

                while (rs.next()) {
                    Integer id = Integer.valueOf(rs.getInt("id"));
                    Date date = rs.getTimestamp("date");
                    String eclass = rs.getString("class");
                    String comm = rs.getString("command");
                    Integer type = Integer.valueOf(rs.getInt("type"));
                    Date validto = rs.getDate("validto");
                    String interval = rs.getString("interval");

                    SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    if (fmt.format(now).equals(fmt.format(date))) {
                        log.info("[scheduler] Executing task [" + id + "] " + eclass + " (" + comm + ")");

                        Class cl = Class.forName("ru.phsystems.irisx.modules." + eclass);
                        Module execute = (Module) cl.newInstance();
                        execute.run(comm);

                        if (type.intValue() == 1) {
                            CronExpression cron = new CronExpression(interval);
                            Date nextRunDate = cron.getNextValidTimeAfter(now);
                            String nextRun = cron.getNextRun(nextRunDate);

                            log.info("Next run at [" + nextRun + "]");
                            Iris.sql.doQuery("UPDATE scheduler SET date='" + nextRun + "' WHERE id='" + id + "'");
                        } else if (type.intValue() == 2) {
                            log.info("Set task to disable");
                            Iris.sql.doQuery("UPDATE scheduler SET enabled='0' WHERE id='" + id + "'");
                        } else {
                            CronExpression cron = new CronExpression(interval);
                            Date nextRunDate = cron.getNextValidTimeAfter(now);
                            String nextRun = cron.getNextRun(nextRunDate);

                            if (validto.before(nextRunDate)) {
                                log.info("Set task to disable");
                                Iris.sql.doQuery("UPDATE scheduler SET enabled='0' WHERE id='" + id + "'");
                            } else {
                                log.info("Next run at [" + nextRun + "]");
                                Iris.sql.doQuery("UPDATE scheduler SET date='" + nextRun + "' WHERE id='" + id + "'");
                            }
                        }

                    }

                }

                rs.close();
            } catch (Exception e) {
                log.info("No scheduled tasks!");
            }

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}