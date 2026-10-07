package com.example.cursorkeep;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;

public final class CursorPositionSaver {
    private CursorPositionSaver() {
    }

    public static volatile boolean ENABLE = true;

    public static volatile long TOLERANCE_MS = 500L;

    private static volatile Double savedOriginalX = null;
    private static volatile Double savedOriginalY = null;

    private static volatile Double savedMiddleX = null;
    private static volatile Double savedMiddleY = null;

    private static volatile Double savedCursorX = null;
    private static volatile Double savedCursorY = null;

    private static volatile long savedAtMs = 0L;

    public static void saveCursorOriginal(double positionedX, double positionedY) {
        savedOriginalX = positionedX;
        savedOriginalY = positionedY;
    }

    public static void saveCursorMiddle(double middleX, double middleY) {
        if (!ENABLE) {
            return;
        }
        Double cursorX = savedOriginalX;
        Double cursorY = savedOriginalY;
        if (cursorX == null || cursorY == null) {
            return;
        }
        savedMiddleX = middleX;
        savedMiddleY = middleY;
        savedCursorX = cursorX;
        savedCursorY = cursorY;
        savedAtMs = System.currentTimeMillis();
    }

    public static double[] loadCursor(double middleX, double middleY) {
        if (!ENABLE) {
            return null;
        }
        Double mX = savedMiddleX;
        Double mY = savedMiddleY;
        Double cX = savedCursorX;
        Double cY = savedCursorY;
        long at = savedAtMs;

        savedMiddleX = null;
        savedMiddleY = null;
        savedCursorX = null;
        savedCursorY = null;

        if (mX == null || mY == null || cX == null || cY == null) {
            return null;
        }
        if (System.currentTimeMillis() - at >= TOLERANCE_MS) {
            return null;
        }
        if (Math.abs(mX - middleX) >= 1.0 || Math.abs(mY - middleY) >= 1.0) {
            return null;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.getWindow() == null) {
            return null;
        }
        InputConstants.grabOrReleaseMouse(
                minecraft.getWindow(),
                InputConstants.CURSOR_NORMAL,
                cX,
                cY
        );
        return new double[]{cX, cY};
    }
}
