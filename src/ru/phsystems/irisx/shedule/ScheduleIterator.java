package ru.phsystems.irisx.shedule;

/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 24.09.12
 * Time: 17:09
 */

import java.util.Date;

/**
 * Implementations of <code>ScheduleIterator</code> specify a schedule as a series of <code>java.util.Date</code> objects.
 */

public interface ScheduleIterator {

    /**
     * Returns the next time that the related {@link SchedulerTask} should be run.
     *
     * @return the next time of execution
     */
    public Date next();
}
