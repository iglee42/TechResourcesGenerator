package fr.iglee42.techresourcesgenerator.utils;

import fr.iglee42.techresourcesgenerator.config.CommonConfigs;

import java.util.Arrays;

public enum ConfigsForType {

    BASIC(GeneratorType.BASIC,CommonConfigs.BASIC_GENERATOR_DELAY.get(),CommonConfigs.BASIC_GENERATOR_GENERATED_ITEM_COUNT.get(),0),
    IRON(GeneratorType.IRON,CommonConfigs.IRON_GENERATOR_DELAY.get(),CommonConfigs.IRON_GENERATOR_GENERATED_ITEM_COUNT.get(),0),
    GOLD(GeneratorType.GOLD,CommonConfigs.GOLD_GENERATOR_DELAY.get(),CommonConfigs.GOLD_GENERATOR_GENERATED_ITEM_COUNT.get(),0),
    DIAMOND(GeneratorType.DIAMOND,CommonConfigs.DIAMOND_GENERATOR_DELAY.get(),CommonConfigs.DIAMOND_GENERATOR_GENERATED_ITEM_COUNT.get(),0),
    NETHERITE(GeneratorType.NETHERITE,CommonConfigs.NETHERITE_GENERATOR_DELAY.get(),CommonConfigs.NETHERITE_GENERATOR_GENERATED_ITEM_COUNT.get(),0),
    MODIUM(GeneratorType.MODIUM,CommonConfigs.MODIUM_GENERATOR_DELAY.get(),CommonConfigs.MODIUM_GENERATOR_GENERATED_ITEM_COUNT.get(),CommonConfigs.MODIUM_GENERATOR_CONSUME.get()),
    DERIUM(GeneratorType.DERIUM,CommonConfigs.DERIUM_GENERATOR_DELAY.get(),CommonConfigs.DERIUM_GENERATOR_GENERATED_ITEM_COUNT.get(),CommonConfigs.DERIUM_GENERATOR_CONSUME.get()),
    BLAZUM(GeneratorType.BLAZUM,CommonConfigs.BLAZUM_GENERATOR_DELAY.get(),CommonConfigs.BLAZUM_GENERATOR_GENERATED_ITEM_COUNT.get(),CommonConfigs.BLAZUM_GENERATOR_CONSUME.get()),
    LAVIUM(GeneratorType.LAVIUM,CommonConfigs.LAVIUM_GENERATOR_DELAY.get(),CommonConfigs.LAVIUM_GENERATOR_GENERATED_ITEM_COUNT.get(),CommonConfigs.LAVIUM_GENERATOR_CONSUME.get()),
    ;

    private GeneratorType type;
    private int delay,itemCount,consumeFE;

    ConfigsForType(GeneratorType type, int delay, int itemCount,int consumeFE) {
        this.type = type;
        this.delay = delay;
        this.itemCount = itemCount;
        this.consumeFE = consumeFE;
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

    public int getConsumeFE() {
        return consumeFE;
    }

    public static ConfigsForType getConfigForType(GeneratorType type){
        return Arrays.stream(values()).filter(c -> c.getType() == type).findFirst().orElse(ConfigsForType.BASIC);
    }
}
