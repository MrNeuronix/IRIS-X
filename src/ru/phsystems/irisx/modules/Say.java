package ru.phsystems.irisx.modules;

/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 22.10.12
 * Time: 10:33
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.phsystems.irisx.utils.Module;
import ru.phsystems.irisx.voice.Synthesizer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Say
        implements Module {
    private static Logger log = LoggerFactory.getLogger(Say.class.getName());

    public void run(String arg) {
        log.info("[voice] " + arg);

        ExecutorService exs = Executors.newFixedThreadPool(10);
        Synthesizer Voice = new Synthesizer(exs);
        Voice.setAnswer(arg);
        try {
            exs.submit(Voice).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}