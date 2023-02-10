package fr.iglee42.techresourcesgenerator.items;

import fr.iglee42.igleelib.api.utils.ModsUtils;
import fr.iglee42.techresourcesgenerator.customize.Gessence;
import fr.iglee42.techresourcesgenerator.customize.Types;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import fr.iglee42.techresourcesgenerator.utils.GessenceType;
import fr.iglee42.techresourcesgenerator.utils.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
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

    private Gessence type;
    public ItemGessence(Gessence type, CreativeModeTab group) {
        super(new Properties().tab(group));
        this.type = type;
    }

    public Gessence getType() {
        return type;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level p_41422_, List<Component> components, TooltipFlag p_41424_) {
        super.appendHoverText(stack, p_41422_, components, p_41424_);
        if (isElectronicGessence(stack.getItem()) && ((ItemGessence)stack.getItem()).getType() == Types.getGessenceType("blazum") && stack.getDisplayName().getString().equals("[Code Lyoko]")){
            components.add(new TextComponent("Right Click").withStyle(ChatFormatting.DARK_GRAY,ChatFormatting.BOLD));
        }
        components.add(new TextComponent("Miniumum Generator Tier : ").withStyle(ChatFormatting.YELLOW)
                .append(new TextComponent(ModsUtils.getUpperName(!(isElectronicGessence(stack.getItem())) ? type.minimumGeneratorType() :
                                (GeneratorType.valueOf(type.minimumGeneratorType().toUpperCase()).getOrder() < GeneratorType.MODIUM.getOrder() ? GeneratorType.MODIUM.name().toLowerCase() : type.name().toLowerCase())
                        , " ")).withStyle(ChatFormatting.GOLD)));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand p_41434_) {
        if (player.getMainHandItem().getItem() instanceof ItemGessence g){
            if (isElectronicGessence(g) && g.getType() == Types.getGessenceType("blazum") && player.getMainHandItem().getDisplayName().getString().equals("[Code Lyoko]")){
                level.playSound(player,player.blockPosition(), ModSounds.EASTER.get(), SoundSource.PLAYERS,0.5f,1.0f);
            }
        }
        return super.use(level, player, p_41434_);
    }


}
