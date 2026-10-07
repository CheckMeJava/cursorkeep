package com.example.cursorkeep;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CursorPositionSaverMod implements ClientModInitializer {
    public static final String MOD_ID = "cursorkeep";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("[cursorkeep] initialized");
    }
}
