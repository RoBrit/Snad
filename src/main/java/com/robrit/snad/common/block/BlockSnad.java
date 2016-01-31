/*
 * BlockSnad.java
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

package com.robrit.snad.common.block;

import com.robrit.snad.common.util.ConfigurationData;
import com.robrit.snad.common.util.LogHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public class BlockSnad extends BlockSand {

  public BlockSnad() {
    super();

    this.setTickRandomly(true);
    this.setHardness(0.5F);
    this.setStepSound(Block.soundTypeSand);
    this.setBlockName("snad");
    this.setCreativeTab(CreativeTabs.tabMisc);
    this.setBlockTextureName("sand");
  }

  @Override
  public void updateTick(World world, int x, int y, int z, Random rand) {
    Block blockAbove = world.getBlock(x, y + 1, z);

    if (!world.blockExists(x, y + 1, z)) {
      return;
    }

    if (blockAbove instanceof BlockReed || blockAbove instanceof BlockCactus) {
      boolean isSameBlockType = true;
      int height = 0;

      while (isSameBlockType) {
        if (world.blockExists(x, y + 1 + height, z)) {
          Block nextPlantBlock = world.getBlock(x, y + 1 + height, z);

          if (nextPlantBlock.getClass() == blockAbove.getClass()) {
            for (int growthAttempts = 0;
                 growthAttempts < ConfigurationData.SPEED_INCREASE_VALUE;
                 growthAttempts++) {
              nextPlantBlock.updateTick(world, x, y + 1 + height, z, rand);
            }
            height++;
          } else {
            isSameBlockType = false;
          }
        } else {
          isSameBlockType = false;
        }
      }
    } else if (blockAbove instanceof IPlantable) {
      blockAbove.updateTick(world, x, y + 2, z, rand);
    }
  }

  @Override
  public boolean canSustainPlant(IBlockAccess world, int x, int y, int z,
                                 ForgeDirection direction, IPlantable plantable) {
    Block plant = plantable.getPlant(world, x, y + 1, z);
    EnumPlantType plantType = plantable.getPlantType(world, x, y + 1, z);

    switch (plantType) {
      case Desert: {
        return true;
      }
      case Water: {
        return world.getBlock(x, y, z).getMaterial() == Material.water
               && world.getBlockMetadata(x, y, z) == 0;
      }
      case Beach: {
        return (world.getBlock(x - 1, y, z).getMaterial() == Material.water ||
                world.getBlock(x + 1, y, z).getMaterial() == Material.water ||
                world.getBlock(x, y, z - 1).getMaterial() == Material.water ||
                world.getBlock(x, y, z + 1).getMaterial() == Material.water);
      }
    }

    return false;
  }
}
