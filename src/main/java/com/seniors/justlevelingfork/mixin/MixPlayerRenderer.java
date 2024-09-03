package com.seniors.justlevelingfork.mixin;

import com.seniors.justlevelingfork.registry.RegistryTitles;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin({PlayerRenderer.class})
public abstract class MixPlayerRenderer extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public MixPlayerRenderer(EntityRendererProvider.Context context, boolean isSlim) {
        super(context, new PlayerModel<>(context.bakeLayer(isSlim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), isSlim), 0.5F);
    }

    @Inject(method = {"renderNameTag(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"}, at = {@At("HEAD")})
    private void render(AbstractClientPlayer entity, Component par2, PoseStack matrices, MultiBufferSource buffer, int light, CallbackInfo info) {
        if (entity.getCustomName() != null) {
            MutableComponent name = Component.literal("<").append(entity.getCustomName().copy().withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.BOLD)).append(Component.literal(">"));
            if (!entity.getCustomName().equals(Component.translatable(RegistryTitles.TITLELESS.get().getKey()))) {
                draw$Text(name, 10, entity, matrices, buffer, light);
            }
        }
    }

    @Unique
    private void draw$Text(Component component, int y, Entity entity, PoseStack matrices, MultiBufferSource buffer, int light) {
        boolean flag = !entity.isDiscrete();
        float f = entity.getNameTagOffsetY();
        matrices.pushPose();
        matrices.translate(0.0F, f, 0.0F);
        matrices.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrices.scale(-0.025F, -0.025F, 0.025F);
        Matrix4f matrix4f = matrices.last().pose();
        float f1 = (Minecraft.getInstance()).options.getBackgroundOpacity(0.25F);
        int j = (int) (f1 * 255.0F) << 24;
        Font font = getFont();
        float f2 = ((float) -font.width(component) / 2);
        font.drawInBatch(component, f2, -y, 553648127, false, matrix4f, buffer, flag ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, j, light);
        if (flag) {
            font.drawInBatch(component, f2, -y, -1, false, matrix4f, buffer, Font.DisplayMode.NORMAL, 0, light);
        }
        matrices.popPose();
    }
}


