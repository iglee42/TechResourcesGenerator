package fr.iglee42.techresourcesgenerator.utils;

import fr.iglee42.techresourcesgenerator.config.CommonConfigs;

import java.util.Arrays;

public enum ConfigsForType {

    BASIC(GeneratorType.BASIC,CommonConfigs.BASIC_GENERATOR_DELAY.get(),fr.iglee42.techresourcesgenerator.config.CommonConfigs.BASIC_GENERATOR_GENERATED_ITEM_COUNT.get()),
    IRON(GeneratorType.IRON,CommonConfigs.IRON_GENERATOR_DELAY.get(),CommonConfigs.IRON_GENERATOR_GENERATED_ITEM_COUNT.get()),
    GOLD(GeneratorType.GOLD,CommonConfigs.GOLD_GENERATOR_DELAY.get(),CommonConfigs.GOLD_GENERATOR_GENERATED_ITEM_COUNT.get()),
    DIAMOND(GeneratorType.DIAMOND,CommonConfigs.DIAMOND_GENERATOR_DELAY.get(),CommonConfigs.DIAMOND_GENERATOR_GENERATED_ITEM_COUNT.get()),
    NETHERITE(GeneratorType.NETHERITE,CommonConfigs.NETHERITE_GENERATOR_DELAY.get(),CommonConfigs.NETHERITE_GENERATOR_GENERATED_ITEM_COUNT.get()),
    ;

    private GeneratorType type;
    private int delay,itemCount;

    ConfigsForType(GeneratorType type, int delay, int itemCount) {
        this.type = type;
        this.delay = delay;
        this.itemCount = itemCount;
    }

    public GeneratorType getType() {
        return type;
    }

    public int getDelay() {
        return delay;
    }

    public int getItemCount() {
        return itemCount;
    }

    public static ConfigsForType getConfigForType(GeneratorType type){
        return Arrays.stream(values()).filter(c -> c.getType() == type).findFirst().orElse(ConfigsForType.BASIC);
    }
}
