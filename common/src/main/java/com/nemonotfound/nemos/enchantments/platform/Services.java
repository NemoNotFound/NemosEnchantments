package com.nemonotfound.nemos.enchantments.platform;

import com.nemonotfound.nemos.enchantments.Constants;
import com.nemonotfound.nemos.enchantments.platform.services.IModLoaderHelper;
import com.nemonotfound.nemos.enchantments.platform.services.IRegistryHelper;

import java.util.ServiceLoader;

public class Services {

    public static final IModLoaderHelper PLATFORM = load(IModLoaderHelper.class);
    public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to load service for " + clazz.getName()));

        Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);

        return loadedService;
    }
}