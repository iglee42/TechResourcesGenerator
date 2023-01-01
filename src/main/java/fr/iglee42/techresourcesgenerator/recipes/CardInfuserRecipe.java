package fr.iglee42.techresourcesgenerator.recipes;

import com.google.gson.JsonObject;
import fr.iglee42.techresourcesbase.api.utils.JsonHelper;
import fr.iglee42.techresourcesgenerator.TechResourcesGenerator;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CardInfuserRecipe implements ICardInfuserRecipe {

     private final ResourceLocation id;
      private final Ingredient base;
      private final Ingredient infuser;
      private final Ingredient result;

    public CardInfuserRecipe(ResourceLocation id, Ingredient base, Ingredient infuser, Ingredient result) {
        this.id = id;
        this.base = base;
        this.infuser = infuser;
        this.result = result;
    }

    @Override
    public boolean matches(IInventory container, World level) {
        return base.test(container.getItem(1)) && infuser.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(IInventory p_44001_) {
        return new ItemStack(result.getItems()[0].getItem());
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(result.getItems()[0].getItem());
    }

    public Ingredient getResult() {
        return result;
    }

    public Ingredient getBase() {
        return base;
    }

    public Ingredient getInfuser() {
        return infuser;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.CARD_INFUSER_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements IRecipeType<CardInfuserRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "card_infuser";
    }
    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<CardInfuserRecipe>{
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(TechResourcesGenerator.MODID,"card_infuser");
        @Override
        public CardInfuserRecipe fromJson(ResourceLocation rs, JsonObject json) {
            Ingredient base = Ingredient.of(JsonHelper.getItem(json,"base"));
            Ingredient infuser = Ingredient.of(JsonHelper.getItem(json,"infuser"));
            Ingredient result = Ingredient.of(JsonHelper.getItem(json,"result"));
            return new CardInfuserRecipe(rs, base,infuser, result);
        }

        @Override
        public CardInfuserRecipe fromNetwork(ResourceLocation resourceLocation, PacketBuffer buffer) {
            Ingredient base = Ingredient.fromNetwork(buffer);
            Ingredient infuser = Ingredient.fromNetwork(buffer);
            Ingredient result = Ingredient.fromNetwork(buffer);
            return new CardInfuserRecipe(resourceLocation, base,infuser, result);
        }
        @Override
        public void toNetwork(PacketBuffer buffer, CardInfuserRecipe recipe) {
            recipe.base.toNetwork(buffer);
            recipe.infuser.toNetwork(buffer);
            recipe.result.toNetwork(buffer);
        }

    }
}
