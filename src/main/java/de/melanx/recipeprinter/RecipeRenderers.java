package de.melanx.recipeprinter;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.HashMap;
import java.util.Map;

public class RecipeRenderers {

    private static final Map<Class<?>, IRecipeRender<?>> renders = new HashMap<>();

    public static boolean isSupported(RecipeType<?> recipeType) {
        return renders.values().stream().anyMatch(render -> render.getRecipeType() == recipeType);
    }

    public static void registerRecipeRender(IRecipeRender<?> render) {
        renders.put(render.getRecipeClass(), render);
    }

    public static <T extends Recipe<?>> IRecipeRender<T> getRecipeRender(T recipe) {
        Class<?> clazz = recipe.getClass();
        IRecipeRender<?> render = null;
        for (Class<?> recipeClass : renders.keySet()) {
            if (recipeClass.isAssignableFrom(clazz)) {
                if (render == null) {
                    render = renders.get(recipeClass);
                } else if (!recipeClass.isInterface() && !recipeClass.isEnum()) {
                    Class<?> sc = clazz;
                    while (true) {
                        sc = sc.getSuperclass();
                        if (sc == Object.class || sc == render.getRecipeClass()) {
                            break;
                        } else if (sc == recipeClass) {
                            render = renders.get(recipeClass);
                            break;
                        }
                    }
                }
            }
        }
        //noinspection unchecked
        return (IRecipeRender<T>) render;
    }
}
