package fr.iglee42.techresourcesgenerator.recipes;

import com.google.gson.JsonObject;
import fr.iglee42.techresourcesbase.utils.JsonHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class CardInfuserRecipe implements Recipe<SimpleContainer> {

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
    public boolean matches(SimpleContainer container, Level level) {
        return base.test(container.getItem(1)) && infuser.test(container.getItem(0));
    }

    @Override
    public ItemStack assemble(SimpleContainer p_44001_) {
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
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CARD_INFUSER_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<CardInfuserRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "card_infuser";
    }
    public static class Serializer implements RecipeSerializer<CardInfuserRecipe> {
        public CardInfuserRecipe fromJson(ResourceLocation rs, JsonObject json) {
            Ingredient base = Ingredient.of(JsonHelper.getItem(json,"base"));
            Ingredient infuser = Ingredient.of(JsonHelper.getItem(json,"infuser"));
            Ingredient result = Ingredient.of(JsonHelper.getItem(json,"result"));
            return new CardInfuserRecipe(rs, base,infuser, result);
        }

        public CardInfuserRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buffer) {
            Ingredient base = Ingredient.fromNetwork(buffer);
            Ingredient infuser = Ingredient.fromNetwork(buffer);
            Ingredient result = Ingredient.fromNetwork(buffer);
            return new CardInfuserRecipe(resourceLocation, base,infuser, result);
        }

        public void toNetwork(FriendlyByteBuf buffer, CardInfuserRecipe recipe) {
            recipe.base.toNetwork(buffer);
            recipe.infuser.toNetwork(buffer);
            recipe.result.toNetwork(buffer);
        }
    }
}
