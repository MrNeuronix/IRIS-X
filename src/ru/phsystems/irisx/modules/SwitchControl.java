package ru.phsystems.irisx.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.phsystems.irisx.Iris;
import ru.phsystems.irisx.devices.Device;
import ru.phsystems.irisx.utils.Module;
import ru.phsystems.irisx.voice.Synthesizer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: nix
 * Date: 21.10.12
 * Time: 2:10
 * To change this template use File | Settings | File Templates.
 */
public class SwitchControl implements Module {

    private static Logger log = LoggerFactory.getLogger(SwitchControl.class.getName());

    public void run(String arg) {
        if (arg.equals("enable")) enableSW();
        if (arg.equals("disable")) disableSW();
    }

    private void enableSW() {
        for (Device dev : Iris.devicesArray) {

            if (dev.getType().equals("Multilevel Power Switch")) {
                if (dev.getValue() != 99) dev.setValue(99);

                log.info("[zwave] Включаю свет!");

                ExecutorService exs = Executors.newFixedThreadPool(10);
                Synthesizer Voice = new Synthesizer(exs);
                Voice.setAnswer("Свет включен");
                try {
                    exs.submit(Voice).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ExecutionException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    private void disableSW() {
        for (Device dev : Iris.devicesArray) {
            if (dev.getType().equals("Multilevel Power Switch")) {
                if (dev.getValue() != 0) dev.setValue(0);
                log.info("[zwave] Выключаю свет!");

                ExecutorService exs = Executors.newFixedThreadPool(10);
                Synthesizer Voice = new Synthesizer(exs);
                Voice.setAnswer("Свет выключен");
                try {
                    exs.submit(Voice).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (ExecutionException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }
}
