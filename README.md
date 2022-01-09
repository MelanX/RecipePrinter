# Recipe Printer
Converts recipes into nice looking images

[![CurseForge](http://cf.way2muchnoise.eu/full_409823_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/recipe-printer)
[![Curseforge](http://cf.way2muchnoise.eu/versions/For%20MC_409823_all.svg)](https://www.curseforge.com/minecraft/mc-mods/recipe-printer)

[![Modrinth](https://modrinth-utils.vercel.app/api/badge/versions?id=jCMrOyTG&logo=true)](https://modrinth.com/mod/recipe-printer)
[![Modrinth](https://modrinth-utils.vercel.app/api/badge/downloads?id=jCMrOyTG&logo=true)](https://modrinth.com/mod/recipe-printer)

## How to use?
### Normal user
1. Download mod from here: https://www.curseforge.com/minecraft/mc-mods/recipe-printer.
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
runtimeOnly fg.deobf("io.github.noeppi_noeppi.mods:LibX:1.18.1-3.1+")
runtimeOnly fg.deobf("de.melanx.recipeprinter:recipeprinter-1.18.1:3.1+")
```
3. Start game.

## Commands
| Command                                         | Result                              |
|-------------------------------------------------|-------------------------------------|
| `/recipeprinter recipes *`                      | prints all recipes                  |
| `/recipeprinter recipes <modid>:*`              | prints all recipes from defined mod |
| `/recipeprinter itemgroup minecraft:<tab_name>` | prints the defined creative tab     |

## How to create an addon?
1. Add Recipe Printer to your dependencies as [described above](#developer) but with `compile` instead of `runtimeOnly`.
2. Create renderer which
   implements [IRecipeRender](https://github.com/MelanX/RecipePrinter/blob/1.18.x/src/main/java/de/melanx/recipeprinter/IRecipeRender.java)
   .
3. Register your renderer in `FMLCommonSetupEvent` with `RecipeRenderers.registerRecipeRender(new CustomRender());`.