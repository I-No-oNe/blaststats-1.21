package net.uhb217.pvp_counter.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

public class ConfigScreen {

    public static Screen configScreen;

    public static Screen getConfigBuilder(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.of("PVP COUNTER Config"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Text.of("General"));

        general.addEntry(entryBuilder
                .startEnumSelector(Text.of("Enable PVP COUNTER"), ConfigData.Modes.class, ConfigFile.getConfig().modState)
                .setTooltip(Text.of("The Mod Settings"))
                .setDefaultValue(ConfigData.Modes.OFF)
                .setSaveConsumer(newValue -> ConfigFile.getConfig().modState = newValue)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.of("Count End Crystals Explosions"), ConfigFile.getConfig().crystalCount)
                .setTooltip(Text.of("Listen To Crystals Explosions."))
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> ConfigFile.getConfig().crystalCount = newValue)
                .build());
        general.addEntry(entryBuilder.startBooleanToggle(Text.of("Count Players Death"), ConfigFile.getConfig().playerDeathsCount)
                .setTooltip(Text.of("Listen To Players Death."))
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> ConfigFile.getConfig().playerDeathsCount = newValue)
                .build());
        builder.setSavingRunnable(ConfigFile::saveConfig);
        configScreen = builder.build();
        return builder.build();
    }
}