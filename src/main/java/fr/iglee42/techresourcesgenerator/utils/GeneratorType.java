package fr.iglee42.techresourcesgenerator.utils;

public enum GeneratorType {

    BASIC(0),
    IRON(1),
    GOLD(2),
    DIAMOND(3),
    NETHERITE(4)
    ;

    private int order;

    GeneratorType(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }


}
