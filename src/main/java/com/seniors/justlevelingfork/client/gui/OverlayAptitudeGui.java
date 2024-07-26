package com.seniors.justlevelingfork.client.gui;

import com.seniors.justlevelingfork.client.core.Aptitudes;
import com.seniors.justlevelingfork.client.core.Utils;
import com.seniors.justlevelingfork.common.capability.AptitudeCapability;
import com.seniors.justlevelingfork.handler.HandlerAptitude;
import com.seniors.justlevelingfork.registry.RegistryCapabilities;
import com.mojang.blaze3d.systems.RenderSystem;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class OverlayAptitudeGui {
    private final Minecraft client = Minecraft.getInstance();
    private static List<Aptitudes> aptitudes = null;
    private static int showTicks = 0;

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onHudRender(CustomizeGuiOverlayEvent.DebugText event) {
        GuiGraphics matrixStack = event.getGuiGraphics();
        if (this.client.level != null && this.client.player != null && showTicks > 0 && this.client.player.getCapability(RegistryCapabilities.APTITUDE).isPresent()) {
            matrixStack.pose().pushPose();
            int xOff = this.client.getWindow().getGuiScaledWidth() / 2;
            int yOff = this.client.getWindow().getGuiScaledHeight() / 4;

            MutableComponent overlayMessage = Component.translatable("overlay.aptitude.message");
            int overlayWidth = this.client.font.width(overlayMessage) / 2;

            RenderSystem.enableBlend();
            for (int i = 0; i < 16; i++) {
                float f = (showTicks < 40) ? (0.003F * i / 40.0F * showTicks) : (0.003F * i);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, f);
                matrixStack.fill(xOff - overlayWidth - 14 - 16 - i, yOff - 11 - 16 - i, xOff + overlayWidth + 14 + 16 - i, yOff + 45 + 16 - i, Color.BLACK.getRGB());
            }
            float alpha = (showTicks < 40) ? (0.025F * showTicks) : 1.0F;
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
            Utils.drawCenterWithShadow(matrixStack, overlayMessage, xOff, yOff, 16733525);

            for (int j = 0; j < aptitudes.size(); j++) {
                Aptitudes abilities = aptitudes.get(j);
                String level = Integer.toString(abilities.getAptitudeLvl());
                boolean met = (AptitudeCapability.get().getAptitudeLevel(abilities.getAptitude()) >= abilities.getAptitudeLvl());

                int x = xOff + j * 24 - aptitudes.size() * 12;
                int y = yOff + 15;

                RenderSystem.enableBlend();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
                matrixStack.blit(abilities.getAptitude().getLockedTexture(abilities.getAptitudeLvl()), x, y, 0.0F, 0.0F, 16, 16, 16, 16);
                Utils.drawCenterWithShadow(matrixStack, level, x + 16, y + 12, met ? 5635925 : 16733525);
            }

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.pose().popPose();
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (showTicks > 0) showTicks--;
    }

    public static void showWarning(String aptitude) {
        aptitudes = HandlerAptitude.getValue(aptitude);
        showTicks = 90;
    }
}


