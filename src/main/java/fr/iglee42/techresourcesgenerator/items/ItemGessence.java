package fr.iglee42.techresourcesgenerator.items;

import fr.iglee42.igleelib.api.utils.ModsUtils;
import fr.iglee42.techresourcesgenerator.customize.Gessence;
import fr.iglee42.techresourcesgenerator.customize.Types;
import fr.iglee42.techresourcesgenerator.utils.GeneratorType;
import fr.iglee42.techresourcesgenerator.utils.ModSounds;
import net.minecraft.client.audio.SoundSource;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
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

    private Gessence type;
    public ItemGessence(Gessence type, ItemGroup group) {
        super(new Properties().tab(group));
        this.type = type;
    }

    public Gessence getType() {
        return type;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World p_41422_, List<ITextComponent> components, ITooltipFlag p_41424_) {
        super.appendHoverText(stack, p_41422_, components, p_41424_);
        components.add(new StringTextComponent("Miniumum Generator Tier : ").withStyle(TextFormatting.YELLOW)
                .append(new StringTextComponent(ModsUtils.getUpperName(!(isElectronicGessence(stack.getItem())) ? type.minimumGeneratorType() :
                                (GeneratorType.valueOf(type.minimumGeneratorType().toUpperCase()).getOrder() < GeneratorType.MODIUM.getOrder() ? GeneratorType.MODIUM.name().toLowerCase() : type.name().toLowerCase())
                        , " ")).withStyle(TextFormatting.GOLD)));
    }


    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand p_41434_) {
        if (player.getMainHandItem().getItem() instanceof ItemGessence){
            ItemGessence g = (ItemGessence) player.getMainHandItem().getItem();
            if (isElectronicGessence(g) && g.getType() == Types.getGessenceType("blazum") && player.getMainHandItem().getDisplayName().getString().equals("[Code Lyoko]")){
                level.playSound(player,player.blockPosition(), ModSounds.EASTER.get(), SoundCategory.PLAYERS,0.5f,1.0f);
            }
        }
        return super.use(level, player, p_41434_);
    }

}
