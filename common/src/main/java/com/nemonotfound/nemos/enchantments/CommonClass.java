package com.nemonotfound.nemos.enchantments;

import com.nemonotfound.nemos.enchantments.entity.attribute.ModAttributes;

public class CommonClass {

    public static void init() {
        Constants.LOG.info("Thank you for using Nemo's Enchantments");

        ModAttributes.init();
    }
}