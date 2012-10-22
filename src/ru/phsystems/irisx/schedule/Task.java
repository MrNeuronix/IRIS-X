package ru.phsystems.irisx.schedule;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.phsystems.irisx.Iris;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j

/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 22.10.12
 * Time: 12:59
 */

public class Task {

    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private Date date;
    @Getter
    @Setter
    private String eclass;
    @Getter
    @Setter
    private String command;
    @Getter
    @Setter
    private int type;
    @Getter
    @Setter
    private Date validto;
    @Getter
    @Setter
    private String interval;
    @Getter
    @Setter
    private int enabled;

    public Task() throws SQLException {
        ResultSet rs = Iris.sql.select("SELECT id FROM scheduler ORDER BY id DESC LIMIT 0,1");
        rs.next();
        int lastid = Integer.valueOf(rs.getInt("id"));
        this.id = lastid++;
        rs.close();

        log.debug("Create new task instance with ID [" + id + "]");
    }

    public Task(int id) throws SQLException {
        log.debug("Create task instance from ID [" + id + "]");

        ResultSet rs = Iris.sql.select("SELECT * FROM scheduler WHERE id='" + id + "'");

        rs.next();

        this.id = id;
        date = rs.getTimestamp("date");
        eclass = rs.getString("class");
        command = rs.getString("command");
        type = Integer.valueOf(rs.getInt("type"));
        validto = rs.getTimestamp("validto");
        interval = rs.getString("interval");
        enabled = Integer.valueOf(rs.getInt("enabled"));

        rs.close();
    }

    public boolean save() {
        log.info("Saving task [" + id + "]");

        if (Iris.sql.doQuery("UPDATE scheduler" +
                "SET id = '" + id + "'," +
                "date='" + getSQLDate(date) + "'," +
                "class = '', command = '" + command + "'," +
                "type = '" + type + "'," +
                "validto = '" + getSQLDate(validto) + "'," +
                "interval = '" + interval + "' WHERE id='" + id + "'")) {
            return true;
        } else {
            return false;
        }
    }

    public String getSQLDate(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return fmt.format(c.getTime());
    }

    public Date nextRunAsDate() throws ParseException {
        Date now = new Date();
        CronExpression cron = new CronExpression(interval);
        Date nextRunDate = cron.getNextValidTimeAfter(now);

        return nextRunDate;
    }

    public String nextRunAsString() throws ParseException {
        Date now = new Date();
        CronExpression cron = new CronExpression(interval);
        Date nextRunDate = cron.getNextValidTimeAfter(now);
        String nextRun = cron.getNextRun(nextRunDate);

        return nextRun;
    }

    public String getDateAsString(Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(date);
    }

    public Date setDateAsString(String date) throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.parse(date);
    }
}
