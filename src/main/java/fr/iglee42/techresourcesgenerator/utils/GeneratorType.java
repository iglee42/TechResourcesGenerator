package fr.iglee42.techresourcesgenerator.utils;

import java.util.Arrays;

public enum GeneratorType {

    BASIC(0),
    IRON(1),
    GOLD(2),
    DIAMOND(3),
    NETHERITE(4),
    MODIUM(5),
    DERIUM(6),
    BLAZUM(7),
    LAVIUM(8),
    ;

    private int order;

    GeneratorType(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public static GeneratorType getByOrder(int order){
        return Arrays.stream(values()).filter(t->t.getOrder() == order).findFirst().orElse(GeneratorType.IRON);
    }


}
