package com.devnemo.nemos.enchantments.platform.services;

public interface IModLoaderHelper {

    String getModLoaderName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();
}