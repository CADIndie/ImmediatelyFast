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
package net.raphimc.immediatelyfast.feature.batching;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgram;

public record RenderSystemState(int texture0, int texture1, int texture2, ShaderProgram program, float[] shaderColor, BlendFuncDepthFuncState blendFuncDepthFunc) {

    public static RenderSystemState current() {
        return new RenderSystemState(
                RenderSystem.getShaderTexture(0),
                RenderSystem.getShaderTexture(1),
                RenderSystem.getShaderTexture(2),
                RenderSystem.getShader(),
                RenderSystem.getShaderColor().clone(),
                BlendFuncDepthFuncState.current()
        );
    }

    public void apply() {
        RenderSystem.setShaderTexture(0, this.texture0);
        RenderSystem.setShaderTexture(1, this.texture1);
        RenderSystem.setShaderTexture(2, this.texture2);
        RenderSystem.setShader(() -> this.program);
        RenderSystem.setShaderColor(this.shaderColor[0], this.shaderColor[1], this.shaderColor[2], this.shaderColor[3]);
        this.blendFuncDepthFunc.apply();
    }

}
