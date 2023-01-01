package fr.iglee42.techresourcesgenerator.items;

import fr.iglee42.techresourcesbase.api.utils.ModsUtils;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

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
    public ItemGessence(GessenceType type, CreativeModeTab group) {
        super(new Properties().tab(group));
        this.type = type;
    }

    public GessenceType getType() {
        return type;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> components, TooltipFlag p_41424_) {
        super.appendHoverText(stack, p_41422_, components, p_41424_);
        components.add(new TextComponent("Miniumum Generator Tier : ").withStyle(ChatFormatting.YELLOW)
                .append(new TextComponent(ModsUtils.getUpperName(!(isElectronicGessence(stack.getItem())) ? type.getMinimumGenerator().name().toLowerCase() :
                        (type.getMinimumGenerator().getOrder() < GeneratorType.MODIUM.getOrder() ? GeneratorType.MODIUM.name().toLowerCase() : type.name().toLowerCase())
                        , " ")).withStyle(ChatFormatting.GOLD)));
    }


}
