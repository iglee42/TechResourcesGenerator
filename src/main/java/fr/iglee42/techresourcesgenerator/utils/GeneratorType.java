package fr.iglee42.techresourcesgenerator.utils;

import java.util.Arrays;

public enum GeneratorType {

    BASIC(0,"basic"),
    IRON(1,"magmatic"),
    GOLD(2,"magmatic"),
    DIAMOND(3,"magmatic"),
    NETHERITE(4,"magmatic"),
    MODIUM(5,"electric"),
    DERIUM(6,"electric"),
    BLAZUM(7,"electric"),
    LAVIUM(8,"electric"),
    ;

    private int order;
    private String type;

    GeneratorType(int order, String type) {
        this.order = order;
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public static GeneratorType getByOrder(int order){
        return Arrays.stream(values()).filter(t->t.getOrder() == order).findFirst().orElse(GeneratorType.IRON);
    }

    public String getType() {
        return type;
    }

    public static GeneratorType getByName(String name){
        return Arrays.stream(values()).filter(t->t.name().toLowerCase().equals(name)).findAny().orElse(GeneratorType.BASIC);
    }


}
