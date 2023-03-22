package fr.iglee42.techresourcesgenerator.utils;

import fr.iglee42.techresourcesgenerator.config.CommonConfigs;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;

public enum ConfigsForType {

    BASIC(GeneratorType.BASIC,CommonConfigs.BASIC_GENERATOR_DELAY,CommonConfigs.BASIC_GENERATOR_GENERATED_ITEM_COUNT,null),
    IRON(GeneratorType.IRON,CommonConfigs.IRON_GENERATOR_DELAY,CommonConfigs.IRON_GENERATOR_GENERATED_ITEM_COUNT,null),
    GOLD(GeneratorType.GOLD,CommonConfigs.GOLD_GENERATOR_DELAY,CommonConfigs.GOLD_GENERATOR_GENERATED_ITEM_COUNT,null),
    DIAMOND(GeneratorType.DIAMOND,CommonConfigs.DIAMOND_GENERATOR_DELAY,CommonConfigs.DIAMOND_GENERATOR_GENERATED_ITEM_COUNT,null),
    NETHERITE(GeneratorType.NETHERITE,CommonConfigs.NETHERITE_GENERATOR_DELAY,CommonConfigs.NETHERITE_GENERATOR_GENERATED_ITEM_COUNT,null),
    MODIUM(GeneratorType.MODIUM,CommonConfigs.MODIUM_GENERATOR_DELAY,CommonConfigs.MODIUM_GENERATOR_GENERATED_ITEM_COUNT,CommonConfigs.MODIUM_GENERATOR_CONSUME),
    DERIUM(GeneratorType.DERIUM,CommonConfigs.DERIUM_GENERATOR_DELAY,CommonConfigs.DERIUM_GENERATOR_GENERATED_ITEM_COUNT,CommonConfigs.DERIUM_GENERATOR_CONSUME),
    BLAZUM(GeneratorType.BLAZUM,CommonConfigs.BLAZUM_GENERATOR_DELAY,CommonConfigs.BLAZUM_GENERATOR_GENERATED_ITEM_COUNT,CommonConfigs.BLAZUM_GENERATOR_CONSUME),
    LAVIUM(GeneratorType.LAVIUM,CommonConfigs.LAVIUM_GENERATOR_DELAY,CommonConfigs.LAVIUM_GENERATOR_GENERATED_ITEM_COUNT,CommonConfigs.LAVIUM_GENERATOR_CONSUME),
    ;

    private GeneratorType type;
    private ForgeConfigSpec.ConfigValue<Integer> delay;
    private ForgeConfigSpec.IntValue itemCount,consumeFE;

    ConfigsForType(GeneratorType type, ForgeConfigSpec.ConfigValue<Integer> delay, ForgeConfigSpec.IntValue itemCount, ForgeConfigSpec.IntValue consumeFE) {
        this.type = type;
        this.delay = delay;
        this.itemCount = itemCount;
        this.consumeFE = consumeFE;
    }

    public GeneratorType getType() {
        return type;
    }

    public int getDelay() {
        return delay.get();
    }

    public int getItemCount() {
        return itemCount.get();
    }

    public int getConsumeFE() {
        return consumeFE.get();
    }

    public static ConfigsForType getConfigForType(GeneratorType type){
        return Arrays.stream(values()).filter(c -> c.getType() == type).findFirst().orElse(ConfigsForType.BASIC);
    }
}
