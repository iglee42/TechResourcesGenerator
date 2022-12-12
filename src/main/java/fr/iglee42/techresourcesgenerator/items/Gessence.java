package fr.iglee42.techresourcesgenerator.items;

import fr.iglee42.techresourcesbase.utils.ModsUtils;
import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class Gessence extends Item {

    public static boolean isGessence(ItemStack stack){
        return stack.getItem() instanceof Gessence;
    }

    private GessenceType type;
    public Gessence(GessenceType type) {
        super(new Properties().tab(TechResourcesGenerator.GENERATOR_GROUP));
        this.type = type;
    }

    public GessenceType getType() {
        return type;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> components, TooltipFlag p_41424_) {
        super.appendHoverText(p_41421_, p_41422_, components, p_41424_);
        components.add(Component.literal("Miniumum Generator Tier : ").withStyle(ChatFormatting.YELLOW)
                .append(Component.literal(ModsUtils.getUpperName(type.getMinimumGenerator().name().toLowerCase(), " ")).withStyle(ChatFormatting.GOLD)));
    }
}
