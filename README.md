# Recipe Printer
Converts recipes into nice looking images

[![Curseforge](http://cf.way2muchnoise.eu/versions/For%20MC_409823_all.svg)](https://www.curseforge.com/minecraft/mc-mods/recipe-printer)
[![CurseForge](http://cf.way2muchnoise.eu/full_409823_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/recipe-printer)

[![Modrinth](https://img.shields.io/modrinth/game-versions/jCMrOyTG?color=00AF5C&label=modrinth&logo=modrinth)](https://modrinth.com/mod/recipe-printer)
[![Modrinth](https://img.shields.io/modrinth/dt/jCMrOyTG?color=00AF5C&logo=modrinth)](https://modrinth.com/mod/recipe-printer)

## How to use?
### Normal user
1. Download mod from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/recipe-printer)
   or [Modrinth](https://modrinth.com/mod/recipe-printer).
2. Put into mods folder.
3. Start game.

### Developer
1. Add the following to your repositories:
```groovy
maven {
    name = "MelanX maven"
    url = "https://maven.melanx.de/"
}
```
2. Add the following to your dependencies:
```groovy
runtimeOnly fg.deobf("org.moddingx:LibX:1.19-4.0.7")
runtimeOnly fg.deobf("de.melanx.recipeprinter:RecipePrinter:1.18.2-3.2+")
```
3. Start game.

## Commands
| Command                                         | Result                              |
|-------------------------------------------------|-------------------------------------|
| `/recipeprinter recipes *`                      | prints all recipes                  |
| `/recipeprinter recipes <modid>:*`              | prints all recipes from defined mod |
| `/recipeprinter itemgroup minecraft:<tab_name>` | prints the defined creative tab     |
| `/recipeprinter fromJei` <recipes>              | prints the recipes by using JEI     |

## How to create an addon?
1. Add Recipe Printer to your dependencies as [described above](#developer) but with `implementation` instead
   of `runtimeOnly`.
2. Create renderer which implements
   [IRecipeRender](https://github.com/MelanX/RecipePrinter/blob/1.18.x/src/main/java/de/melanx/recipeprinter/IRecipeRender.java)
   .
3. Register your renderer in `FMLCommonSetupEvent` with `RecipeRenderers.registerRecipeRender(new CustomRender());`.
