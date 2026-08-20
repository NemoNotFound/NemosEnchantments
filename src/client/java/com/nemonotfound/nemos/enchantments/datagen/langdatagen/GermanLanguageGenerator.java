package com.nemonotfound.nemos.enchantments.datagen.langdatagen;

import com.nemonotfound.nemos.enchantments.enchantment.Enchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

import static com.nemonotfound.nemos.enchantments.datagen.langdatagen.EnglishLanguageGenerator.getEnchantmentTranslationKey;
import static com.nemonotfound.nemos.enchantments.datagen.langdatagen.EnglishLanguageGenerator.getEnchantmentDescriptionTranslationKey;

public class GermanLanguageGenerator extends FabricLanguageProvider {

    public GermanLanguageGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, "de_de", completableFuture);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SOUL_BINDING), "Seelenbindung");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.SOUL_BINDING), "Seelengebundene Gegenstände werden nach dem Tod nicht aus dem Inventar fallen.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.CLIMBER), "Kletterer");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.CLIMBER), "Lässt dich schneller auf Leitern und Gerüste klettern.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SPRINTER), "Sprinter");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.SPRINTER), "Erhöht deine Bewegungsgeschwindigkeit.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.REAPER), "Mäher");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.REAPER), "Erntet mehrere Pflanzen in der Nähe gleichzeitig.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.FARMERS_KNOWLEDGE), "Wissen des Bauern");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.FARMERS_KNOWLEDGE), "Verhindert, dass du Pflanzen erntest, bevor sie ausgewachsen sind.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.REPLANTING), "Wiederbepflanzung");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.REPLANTING), "Pflanzt geerntete Pflanzen neu an.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.MAGMA_WALKER), "Magmaläufer");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.MAGMA_WALKER), "Verwandelt Lava in deiner Nähe beim Gehen in Magma.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SNOW_WALKER), "Schneeläufer");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.SNOW_WALKER), "Lässt dich über Pulverschnee laufen.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.COLLECTOR), "Sammler");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.COLLECTOR), "Legt abgebaute Blöcke direkt in dein Inventar.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.WISDOM), "Weisheit");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.WISDOM), "Erhöht die von Blöcken und Kreaturen erhaltene Erfahrung.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.FELLING), "Baumfäller");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.FELLING), "Fällt einen ganzen Baum durch das Abbauen eines Stammes.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.HEAD_HUNTER), "Kopfjäger");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.HEAD_HUNTER), "Getötete Kreaturen haben eine geringe Chance ihre Köpfe fallen zu lassen");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SOUL_TOUCH), "Seelenbehutsamkeit");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.SOUL_TOUCH), "Lässt dich einen Monsterspawner abbauen und aufsammeln.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.CAMOUFLAGE), "Tarnung");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.CAMOUFLAGE), "Verringert die Monstersichtbarkeit.");

        translationBuilder.add("tooltip.nemos_enchantments.hold_shift", "Halte Umschalt für Verzauberungsbeschreibungen");
        translationBuilder.add("attribute.name.climbing_speed", "Klettergeschwindigkeit");
        translationBuilder.add("attribute.name.monster_visibility", "Monstersichtbarkeit");
    }
}
