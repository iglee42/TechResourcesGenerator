package fr.iglee42.techresourcesgenerator.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfigs {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue BASIC_GENERATOR_GENERATED_ITEM_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> BASIC_GENERATOR_DELAY;
    public static final ForgeConfigSpec.IntValue IRON_GENERATOR_GENERATED_ITEM_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> IRON_GENERATOR_DELAY;
    public static final ForgeConfigSpec.IntValue GOLD_GENERATOR_GENERATED_ITEM_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> GOLD_GENERATOR_DELAY;
    public static final ForgeConfigSpec.IntValue DIAMOND_GENERATOR_GENERATED_ITEM_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> DIAMOND_GENERATOR_DELAY;
    public static final ForgeConfigSpec.IntValue NETHERITE_GENERATOR_GENERATED_ITEM_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> NETHERITE_GENERATOR_DELAY;

    static {
        BUILDER.push("Basic Generator");
        BASIC_GENERATOR_GENERATED_ITEM_COUNT = BUILDER.comment("The number of items was generated !")
                .defineInRange("basic_generated_count",1,1,64);
        BASIC_GENERATOR_DELAY = BUILDER.comment("The delay beetween items ! Minimum : 1")
                .define("basic_generated_delay",20);
        BUILDER.pop();
        BUILDER.push("magmatic generators");
        BUILDER.push("iron");
        IRON_GENERATOR_GENERATED_ITEM_COUNT = BUILDER.comment("The number of items was generated !")
                .defineInRange("iron_generated_count",2,1,64);
        IRON_GENERATOR_DELAY = BUILDER.comment("The delay beetween items ! Minimum : 1")
                .define("iron_generated_delay",17);
        BUILDER.pop();
        BUILDER.push("gold");
        GOLD_GENERATOR_GENERATED_ITEM_COUNT = BUILDER.comment("The number of items was generated !")
                .defineInRange("gold_generated_count",3,1,64);
        GOLD_GENERATOR_DELAY = BUILDER.comment("The delay beetween items ! Minimum : 1")
                .define("gold_generated_delay",15);
        BUILDER.pop();
        BUILDER.push("diamond");
        DIAMOND_GENERATOR_GENERATED_ITEM_COUNT = BUILDER.comment("The number of items was generated !")
                .defineInRange("diamond_generated_count",4,1,64);
        DIAMOND_GENERATOR_DELAY = BUILDER.comment("The delay beetween items ! Minimum : 1")
                .define("diamond_generated_delay",13);
        BUILDER.pop();
        BUILDER.push("netherite");
        NETHERITE_GENERATOR_GENERATED_ITEM_COUNT = BUILDER.comment("The number of items was generated !")
                .defineInRange("netherite_generated_count",8,1,64);
        NETHERITE_GENERATOR_DELAY = BUILDER.comment("The delay beetween items ! Minimum : 1")
                .define("netherite_generated_delay",10);
        BUILDER.pop(2);




        SPEC = BUILDER.build();
    }
}
