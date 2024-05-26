/*
 * This file is part of ImmediatelyFast - https://github.com/RaphiMC/ImmediatelyFast
 * Copyright (C) 2023-2024 RK_01/RaphiMC and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.raphimc.immediatelyfast.injection.mixins.hud_batching.compat.iceberg;

import com.mojang.blaze3d.systems.RenderSystem;
import net.lenni0451.reflect.Objects;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Matrix4f;
import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;
import net.raphimc.immediatelyfast.feature.batching.BatchingRenderLayers;
import net.raphimc.immediatelyfast.feature.batching.BlendFuncDepthFuncState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("UnresolvedMixinReference")
@Mixin(targets = "com.anthonyhilyard.iceberg.util.GuiHelper")
@Pseudo
public abstract class MixinIceberg_GuiHelper {

    @Shadow
    private static void drawGradientRect(Matrix4f matrix, BufferBuilder bufferBuilder, int left, int top, int right, int bottom, int zLevel, int startColor, int endColor) {
    }

    @Inject(method = "drawGradientRect(Lnet/minecraft/util/math/Matrix4f;IIIIIII)V", at = @At("HEAD"), cancellable = true)
    private static void drawIntoBuffer(Matrix4f matrix, int zLevel, int left, int top, int right, int bottom, int startColor, int endColor, CallbackInfo ci) {
        if (BatchingBuffers.FILL_CONSUMER != null) {
            ci.cancel();

            final float[] shaderColor = RenderSystem.getShaderColor();
            final int argb = (int) (shaderColor[3] * 255) << 24 | (int) (shaderColor[0] * 255) << 16 | (int) (shaderColor[1] * 255) << 8 | (int) (shaderColor[2] * 255);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            final VertexConsumer vertexConsumer = BatchingBuffers.FILL_CONSUMER.getBuffer(BatchingRenderLayers.FILLED_QUAD.apply(BlendFuncDepthFuncState.current()));
            drawGradientRect(matrix, Objects.cast(vertexConsumer, BufferBuilder.class), left, top, right, bottom, zLevel, ColorHelper.Argb.mixColor(startColor, argb), ColorHelper.Argb.mixColor(endColor, argb));
            RenderSystem.disableBlend();
        }
    }

}
