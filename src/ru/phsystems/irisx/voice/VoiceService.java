package ru.phsystems.irisx.voice;

import javaFlacEncoder.FLAC_FileEncoder;

import java.io.*;
import java.util.Properties;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * Author: Nikolay A. Viguro
 * Date: 09.09.12
 * Time: 13:57
 * License: GPL v3
 */
public class VoiceService implements Runnable {

    public VoiceService() {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public synchronized void run() {

        System.out.println("[record] Service started");

        Properties prop = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream("./conf/main.property");
            prop.load(is);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        int threads = Integer.valueOf(prop.getProperty("recordStreams"));

        System.out.println("[record] Configured to run " + threads + " threads");

        // Запускам потоки с записью с промежутком в 1с
        for (int i = 1; i <= threads; i++) {
            System.out.println("[record] Start thread " + i);

            new Thread(new Runnable() {

                @Override
                public void run() {

                    Random randomGenerator = new Random();
                    String strFilename = "infile-" + randomGenerator.nextInt(1000) + ".wav";
                    File outputFile = new File("./data/" + strFilename);

                    // Тут захват и обработка звука
                    //////////////////////////////////

                    // указываем в конструкторе ProcessBuilder,
                    // что нужно запустить программу  rec (из пакета sox)
                    ProcessBuilder procBuilder = new ProcessBuilder("rec", "-q", "-c", "1", "-r", "16000", "./data/" + strFilename, "trim", "0", "5");

                    // перенаправляем стандартный поток ошибок на
                    // стандартный вывод
                    procBuilder.redirectErrorStream(true);

                    while (1 == 1) {
                        httpPOST SendFile = new httpPOST();

                        // запуск программы
                        Process process = null;
                        try {
                            process = procBuilder.start();
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                        // читаем стандартный поток вывода
                        // и выводим на экран
                        InputStream stdout = process.getInputStream();
                        InputStreamReader isrStdout = new InputStreamReader(stdout);
                        BufferedReader brStdout = new BufferedReader(isrStdout);

                        String line = null;
                        try {
                            while ((line = brStdout.readLine()) != null) {
                                System.out.println(line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                        // ждем пока завершится вызванная программа
                        // и сохраняем код, с которым она завершилась в
                        // в переменную exitVal
                        try {
                            int exitVal = process.waitFor();
                        } catch (InterruptedException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                        // Перекодируем в FLAC
                        FLAC_FileEncoder encoder1 = new FLAC_FileEncoder();
                        File infile = outputFile;
                        File outfile = new File("./data/" + strFilename + ".flac");
                        encoder1.useThreads(true);
                        encoder1.encode(infile, outfile);

                        // передаем на обработку в гугл
                        String googleSpeechAPIResponse = SendFile.postFile(System.getProperty("user.dir") + "/data/" + strFilename + ".flac");

                        // debug
                        try {
                            if (!googleSpeechAPIResponse.contains("\"utterance\":")) {
                                // System.err.println("[record] Recognizer: No Data");
                            } else {
                                // Include -> System.out.println(wGetResponse); // to view the Raw output
                                int startIndex = googleSpeechAPIResponse.indexOf("\"utterance\":") + 13; //Account for term "utterance":"<TARGET>","confidence"
                                int stopIndex = googleSpeechAPIResponse.indexOf(",\"confidence\":") - 1; //End position
                                String command = googleSpeechAPIResponse.substring(startIndex, stopIndex);

                                // Determine Confidence
                                startIndex = stopIndex + 15;
                                stopIndex = googleSpeechAPIResponse.indexOf("}]}") - 1;
                                double confidence = Double.parseDouble(googleSpeechAPIResponse.substring(startIndex, stopIndex));
                                System.out.println("[data] Utterance : " + command.toUpperCase() + "\n[data] Confidence Level: " + (confidence * 100));
                            }
                        } catch (NullPointerException npE) {
                        }

                        // Подчищаем за собой
                        outputFile.delete();
                        infile.delete();

                        /////////////////////////////////
                    }
                }
            }).start();

            // Пауза в 1с перед запуском следующего потока
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }
}
