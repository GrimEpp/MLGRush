package me.grimepp.system;

import me.grimepp.MLGRush;


public class Default {
    protected Config getConfig() {
    return MLGRush.getInstance().getCM();
}
    public static Config getConfigs() {return MLGRush.getInstance().getCM();}
}
