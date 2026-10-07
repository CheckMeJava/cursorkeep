package com.example.cursorkeep.mixin;

import com.example.cursorkeep.CursorPositionSaver;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
    @Shadow
    private double xpos;

    @Shadow
    private double ypos;

    @Inject(method = "grabMouse", at = @At(value = "HEAD"))
    private void cursorkeep$saveOriginal(CallbackInfo ci) {
        CursorPositionSaver.saveCursorOriginal(this.xpos, this.ypos);
    }

    @Inject(
        method = "grabMouse",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;getWindow()Lcom/mojang/blaze3d/platform/Window;",
            ordinal = 2
        )
    )
    private void cursorkeep$saveMiddle(CallbackInfo ci) {
        CursorPositionSaver.saveCursorMiddle(this.xpos, this.ypos);
    }

    @Inject(
        method = "releaseMouse",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;getWindow()Lcom/mojang/blaze3d/platform/Window;",
            ordinal = 2
        )
    )
    private void cursorkeep$restore(CallbackInfo ci) {
        double[] restored = CursorPositionSaver.loadCursor(this.xpos, this.ypos);
        if (restored == null) {
            return;
        }
        this.xpos = restored[0];
        this.ypos = restored[1];
    }
}
