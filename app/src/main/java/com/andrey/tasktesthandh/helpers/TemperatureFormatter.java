package com.andrey.tasktesthandh.helpers;



public class TemperatureFormatter {
    public static String format(float temperature) {
        return String.valueOf(Math.round(temperature)) + "°";
    }
}