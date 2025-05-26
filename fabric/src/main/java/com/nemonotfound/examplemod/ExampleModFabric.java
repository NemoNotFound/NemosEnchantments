package com.nemonotfound.examplemod;

import net.fabricmc.api.ModInitializer;

public class ExampleModFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        CommonClass.init();
    }
}
