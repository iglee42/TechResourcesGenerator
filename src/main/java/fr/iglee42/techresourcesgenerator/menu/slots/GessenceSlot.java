package fr.iglee42.techresourcesgenerator.menu.slots;

import fr.iglee42.techresourcesgenerator.items.ItemGessence;
import fr.iglee42.techresourcesgenerator.tiles.generator.GeneratorTile;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class GessenceSlot extends SlotItemHandler {

    private final GeneratorTile generatorTile;

    private final boolean acceptOnlyElectronic;

    public GessenceSlot(@Nonnull IItemHandler handler, int id, int x, int y, GeneratorTile blockEntity) {
        this(handler,id,x,y,blockEntity,false);
    }
    public GessenceSlot(@Nonnull IItemHandler handler, int id, int x, int y, GeneratorTile blockEntity,boolean acceptOnlyElectronic) {
        super(handler,id,x,y);
        this.generatorTile = blockEntity;
        this.acceptOnlyElectronic = acceptOnlyElectronic;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return ItemGessence.isGessence(stack.getItem()) && GessenceType.isGeneratorValidForGessence(stack,generatorTile.getGeneratorType()) && (acceptOnlyElectronic ? ItemGessence.isElectronicGessence(stack.getItem()) : ItemGessence.isNormalGessence(stack.getItem()));
    }
}
