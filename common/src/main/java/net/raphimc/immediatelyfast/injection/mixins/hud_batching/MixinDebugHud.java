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
package net.raphimc.immediatelyfast.injection.mixins.hud_batching;

import net.minecraft.client.gui.hud.DebugHud;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.feature.batching.BatchingBuffers;
import net.raphimc.immediatelyfast.injection.processors.InjectAboveEverything;
import net.raphimc.immediatelyfast.injection.processors.InjectOnAllReturns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DebugHud.class, priority = 1500)
public abstract class MixinDebugHud {

    @InjectAboveEverything
    @Inject(method = "render", at = @At("HEAD"))
    private void beginBatching(CallbackInfo ci) {
        if (ImmediatelyFast.runtimeConfig.hud_batching) {
            BatchingBuffers.beginDebugHudBatching();
        } else if (ImmediatelyFast.config.experimental_universal_hud_batching && BatchingBuffers.isHudBatching()) {
            BatchingBuffers.endHudBatching();
            BatchingBuffers.beginDebugHudBatching();
        }
    }

    @InjectOnAllReturns
    @Inject(method = "render", at = @At("RETURN"))
    private void endBatching(CallbackInfo ci) {
        if (ImmediatelyFast.runtimeConfig.hud_batching) {
            BatchingBuffers.endDebugHudBatching();
        } else if (ImmediatelyFast.config.experimental_universal_hud_batching && BatchingBuffers.isHudBatching()) {
            BatchingBuffers.endDebugHudBatching();
            BatchingBuffers.beginHudBatching();
        }
    }

}
