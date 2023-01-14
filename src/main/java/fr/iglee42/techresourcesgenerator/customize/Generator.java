package fr.iglee42.techresourcesgenerator.customize;

import java.util.Objects;

public class Generator {
    private final boolean isInModBase;
    private final String name,centerTexture;
    private final boolean hasCustomTexture;
    private final String generatorType;
    private final int delay,itemCount,consumed;

    public Generator(boolean isInModBase, String name, String centerTexture, boolean hasCustomTexture, String generatorType, int delay, int itemCount, int consumed) {
        this.isInModBase = isInModBase;
        this.name = name;
        this.centerTexture = centerTexture;
        this.hasCustomTexture = hasCustomTexture;
        this.generatorType = generatorType;
        this.delay = delay;
        this.itemCount = itemCount;
        this.consumed = consumed;
    }

    public boolean isInModBase(){
        return isInModBase;
    }

    public String name(){
        return name;
    }

    public String centerTexture(){
        return centerTexture;
    }

    public boolean hasCustomTexture(){
        return hasCustomTexture;
    }

    public String generatorType(){
        return generatorType;
    }

    public int delay(){return delay;}
    public int itemCount(){return itemCount;}
    public int consumed(){return consumed;}

    public int getOrder(){
        return Types.GENERATORS_ORDERS.get(name());
    }


    public static Generator getByName(String name){
        return Types.GENERATORS.stream().filter(g-> Objects.equals(g.name(), name)).findFirst().orElse(null);
    }

    public static Generator getByOrder(int order){
        return getByName(Types.GENERATORS_ORDERS.entrySet().stream().filter(entry->entry.getValue().equals(order)).findFirst().get().getKey());
    }

}
