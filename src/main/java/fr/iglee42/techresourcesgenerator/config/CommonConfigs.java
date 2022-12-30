package fr.iglee42.techresourcesgenerator.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfigs {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue BASIC_GENERATOR_GENERATED_ITEM_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> BASIC_GENERATOR_DELAY;


    /*
       MAGMATICS GENERATORS
     */
    public static final ForgeConfigSpec.IntValue IRON_GENERATOR_GENERATED_ITEM_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> IRON_GENERATOR_DELAY;

    public static final ForgeConfigSpec.IntValue GOLD_GENERATOR_GENERATED_ITEM_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> GOLD_GENERATOR_DELAY;

    public static final ForgeConfigSpec.IntValue DIAMOND_GENERATOR_GENERATED_ITEM_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> DIAMOND_GENERATOR_DELAY;

    public static final ForgeConfigSpec.IntValue NETHERITE_GENERATOR_GENERATED_ITEM_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> NETHERITE_GENERATOR_DELAY;

    /*
        ELECTRICS GENERATORS
     */
    public static final ForgeConfigSpec.IntValue MODIUM_GENERATOR_GENERATED_ITEM_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> MODIUM_GENERATOR_DELAY;
    public static final ForgeConfigSpec.IntValue MODIUM_GENERATOR_CONSUME;

    public static final ForgeConfigSpec.IntValue DERIUM_GENERATOR_GENERATED_ITEM_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> DERIUM_GENERATOR_DELAY;
    public static final ForgeConfigSpec.IntValue DERIUM_GENERATOR_CONSUME;

    public static final ForgeConfigSpec.IntValue BLAZUM_GENERATOR_GENERATED_ITEM_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> BLAZUM_GENERATOR_DELAY;
    public static final ForgeConfigSpec.IntValue BLAZUM_GENERATOR_CONSUME;

    public static final ForgeConfigSpec.IntValue LAVIUM_GENERATOR_GENERATED_ITEM_COUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> LAVIUM_GENERATOR_DELAY;
    public static final ForgeConfigSpec.IntValue LAVIUM_GENERATOR_CONSUME;



    static {
        BUILDER.push("basic_generator");
        BASIC_GENERATOR_GENERATED_ITEM_COUNT = BUILDER.comment("The number of items was generated !")
                .defineInRange("basic_generated_count",1,1,64);
        BASIC_GENERATOR_DELAY = BUILDER.comment("The delay beetween items ! Minimum : 1")
                .define("basic_generated_delay",20);
        BUILDER.pop();
        /*
        MAGMATICS GENERATORS
         */
        BUILDER.push("magmatic_generators");
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
        /*
        ELECTRICS GENERATORS
         */
        BUILDER.push("electric_generators");
        BUILDER.push("modium");
        MODIUM_GENERATOR_GENERATED_ITEM_COUNT = BUILDER.comment("The number of items was generated !")
                .defineInRange("modium_generated_count",16,1,64);
        MODIUM_GENERATOR_DELAY = BUILDER.comment("The delay beetween items ! Minimum : 1")
                .define("modium_generated_delay",10);
        MODIUM_GENERATOR_CONSUME = BUILDER.comment("FE consumed per ticks")
                        .defineInRange("modium_consume_ticks",8192,1,Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("derium");
        DERIUM_GENERATOR_GENERATED_ITEM_COUNT = BUILDER.comment("The number of items was generated !")
                .defineInRange("derium_generated_count",32,1,64);
        DERIUM_GENERATOR_DELAY = BUILDER.comment("The delay beetween items ! Minimum : 1")
                .define("derium_generated_delay",7);
        DERIUM_GENERATOR_CONSUME = BUILDER.comment("FE consumed per ticks")
                .defineInRange("derium_consume_ticks",4096,1,Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("blazum");
        BLAZUM_GENERATOR_GENERATED_ITEM_COUNT = BUILDER.comment("The number of items was generated !")
                .defineInRange("blazum_generated_count",48,1,64);
        BLAZUM_GENERATOR_DELAY = BUILDER.comment("The delay beetween items ! Minimum : 1")
                .define("blazum_generated_delay",5);
        BLAZUM_GENERATOR_CONSUME = BUILDER.comment("FE consumed per ticks")
                .defineInRange("blazum_consume_ticks",2048,1,Integer.MAX_VALUE);
        BUILDER.pop();
        BUILDER.push("lavium");
        LAVIUM_GENERATOR_GENERATED_ITEM_COUNT = BUILDER.comment("The number of items was generated !")
                .defineInRange("lavium_generated_count",64,1,64);
        LAVIUM_GENERATOR_DELAY = BUILDER.comment("The delay beetween items ! Minimum : 1")
                .define("lavium_generated_delay",3);
        LAVIUM_GENERATOR_CONSUME = BUILDER.comment("FE consumed per ticks")
                .defineInRange("lavium_consume_ticks",1024,1,Integer.MAX_VALUE);
        BUILDER.pop(2);


        SPEC = BUILDER.build();
    }
}
