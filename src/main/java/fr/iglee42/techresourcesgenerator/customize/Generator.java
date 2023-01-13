package fr.iglee42.techresourcesgenerator.customize;

import java.util.Objects;

public record Generator(boolean isInModBase, String name, String centerTexture, boolean hasCustomTexture, String generatorType, int delay, int itemCount,int consumed) {
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
