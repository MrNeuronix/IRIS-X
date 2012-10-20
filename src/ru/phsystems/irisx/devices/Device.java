package ru.phsystems.irisx.devices;

import ru.phsystems.irisx.Iris;

/**
 * IRIS-X Project
 * Author: Nikolay A. Viguro
 * WWW: smart.ph-systems.ru
 * E-Mail: nv@ph-systems.ru
 * Date: 18.09.12
 * Time: 13:40
 */

public class Device {

    private String name;
    private int node;
    private int zone;
    private int value;
    private String type;
    private String manufName;

    public Device(String inDevice) {
        String[] device = inDevice.split("~");

        name = device[1];
        node = Integer.valueOf(device[2]);

        if (device[3].equals("")) {
            zone = 0;
        } else {
            zone = Integer.valueOf(device[3]);
        }

        manufName = device[5];
        if (device[6].equals("NONE")) {
            value = -1;
        } else {
            value = Integer.valueOf(device[6]);
        }
        type = device[4];
    }

    public String getName() {
        return this.name;
    }

    public int getNode() {
        return this.node;
    }

    public int getZone() {
        return this.zone;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
        Iris.zwaveSocketOut.println("DEVICE~" + node + "~" + value + "~" + type);
    }

    public String getType() {
        return this.type;
    }

    public String getManufName() {
        return this.manufName;
    }

    public String getStatus() {
        return "Онлайн";
    }

}
