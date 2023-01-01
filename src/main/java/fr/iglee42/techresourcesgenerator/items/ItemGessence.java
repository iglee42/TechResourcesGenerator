package fr.iglee42.techresourcesgenerator.items;

import fr.iglee42.techresourcesbase.api.utils.ModsUtils;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGessence extends Item {

    public static boolean isGessence(Item item){
        return item instanceof ItemGessence;
    }

    public static boolean isNormalGessence(Item item){
        return ForgeRegistries.ITEMS.getKey(item).getPath().endsWith("_gessence");
    }
    public static boolean isElectronicGessence(Item item){
        return ForgeRegistries.ITEMS.getKey(item).getPath().endsWith("_card");
    }

    private GessenceType type;
    public ItemGessence(GessenceType type, ItemGroup group) {
        super(new Properties().tab(group));
        this.type = type;
    }

    public GessenceType getType() {
        return type;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World p_41422_, List<ITextComponent> components, ITooltipFlag p_41424_) {
        super.appendHoverText(stack, p_41422_, components, p_41424_);
        components.add(new StringTextComponent("Miniumum Generator Tier : ").withStyle(TextFormatting.YELLOW)
                .append(new StringTextComponent(ModsUtils.getUpperName(!(isElectronicGessence(stack.getItem())) ? type.getMinimumGenerator().name().toLowerCase() :
                        (type.getMinimumGenerator().getOrder() < GeneratorType.MODIUM.getOrder() ? GeneratorType.MODIUM.name().toLowerCase() : type.name().toLowerCase())
                        , " ")).withStyle(TextFormatting.GOLD)));
    }


}
