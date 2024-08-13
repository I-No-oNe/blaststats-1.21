package net.uhb217.pvp_counter.config;

import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "pvp-counter")
public class ConfigData {

    public boolean crystalCount = true;
    public boolean playerDeathsCount = true;
    public Modes modState = Modes.OFF;

//    Maybe add Sword Swings per second, etc

    public enum Modes {
        OFF,
        REVERSE,
        ON
    }
}
