/*
 * CommonProxy.java
 *
 * Copyright (c) 2016 TheRoBrit
 *
 * SNAD is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SNAD is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.robrit.snad.common.proxy;

import com.robrit.snad.common.block.BlockSnad;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public abstract class CommonProxy implements IProxy {

  public static final BlockSnad blockSnad = new BlockSnad();

  @Override
  public void registerBlocks() {
    GameRegistry.registerBlock(blockSnad, blockSnad.getUnlocalizedName());
  }

  @Override
  public void registerRecipes() {
    GameRegistry.addRecipe(new ItemStack(blockSnad), "S", "S", 'S', Blocks.sand);
  }
}
