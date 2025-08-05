package com.devnemo.nemos.enchantments;

import com.devnemo.nemos.enchantments.entity.attribute.NemosAttributes;

public class CommonClass {

    public static void init() {
        Constants.LOG.info("Thank you for using Nemo's Enchantments");

        NemosAttributes.init();
    }
}