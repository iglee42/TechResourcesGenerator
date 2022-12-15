package fr.iglee42.techresourcesgenerator.menu.slots;

import fr.iglee42.techresourcesgenerator.items.Gessence;
import fr.iglee42.techresourcesgenerator.tiles.generator.ElectricGeneratorTile;
import fr.iglee42.techresourcesgenerator.tiles.generator.GeneratorTile;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class GessenceSlot extends SlotItemHandler {

    private final GeneratorTile generatorTile;

    private final boolean acceptOnlyElectronic;

    public GessenceSlot(@NotNull IItemHandler handler, int id, int x, int y, GeneratorTile blockEntity) {
        this(handler,id,x,y,blockEntity,false);
    }
    public GessenceSlot(@NotNull IItemHandler handler, int id, int x, int y, GeneratorTile blockEntity,boolean acceptOnlyElectronic) {
        super(handler,id,x,y);
        this.generatorTile = blockEntity;
        this.acceptOnlyElectronic = acceptOnlyElectronic;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return Gessence.isGessence(stack.getItem()) && GessenceType.isGeneratorValidForGessence(stack,generatorTile.getGeneratorType()) && (acceptOnlyElectronic ? Gessence.isElectronicGessence(stack.getItem()) : true);
    }
}
