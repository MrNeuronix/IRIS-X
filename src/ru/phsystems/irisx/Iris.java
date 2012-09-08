package ru.phsystems.irisx;
/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 08.09.12
 * Time: 15:24
 * License: GPL v3
 */

public class Iris {

    public static void main(String[] args) {

        try{
            System.out.println("[iris] System starting");

            // Запускам поток с веб-интерфейсом
            Web www = new Web();

            System.out.println("[iris] Done!");
        }
        catch(Exception ee){ee.printStackTrace();}
    }

}
