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

import java.util.List;
import java.util.Random;

import com.robrit.snad.common.item.IMetaBlockSnad;
import com.robrit.snad.common.util.ConfigurationData;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockReed;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockSnad extends BlockFalling implements IMetaBlockSnad {

  public static final PropertyEnum<BlockSnad.EnumType> VARIANT =
      PropertyEnum.create("variant", BlockSnad.EnumType.class);

  public BlockSnad() {
    super(Material.SAND);
    setTickRandomly(true);
    setHardness(0.5F);
    setSoundType(blockSoundType.SAND);
    setCreativeTab(CreativeTabs.MISC);
    setUnlocalizedName("snad");
    setDefaultState(blockState.getBaseState().withProperty(VARIANT, EnumType.SAND));
  }

  @Override
  public int damageDropped(IBlockState state) {
    return getMetaFromState(state);
  }

  @Override
  public MapColor getMapColor(IBlockState state) {
    return state.getValue(VARIANT).getMapColor();
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(VARIANT, meta == 0 ? EnumType.SAND : EnumType.RED_SAND);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(VARIANT).getMetadata();
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, VARIANT);
  }

  @Override
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
    for (BlockSnad.EnumType blockType : BlockSnad.EnumType.values()) {
      list.add(new ItemStack(itemIn, 1, blockType.getMetadata()));
    }
  }

  @Override
  public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player){
    return new ItemStack(Item.getItemFromBlock(this), 1,
                         getMetaFromState(world.getBlockState(pos)));
  }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
    if (!world.isRemote) {
      checkFallable(world, pos);
    }

    Block blockAbove = world.getBlockState(pos.up()).getBlock();

    if (blockAbove == null) {
      return;
    }

    if (blockAbove instanceof BlockReed || blockAbove instanceof BlockCactus) {
      boolean isSameBlockType = true;
      int height = 1;

      while (isSameBlockType) {
        if (world.getBlockState(pos.up(height)).getBlock() != null) {
          Block nextPlantBlock = world.getBlockState(pos.up(height)).getBlock();
          if (nextPlantBlock.getClass() == blockAbove.getClass()) {
            for (int growthAttempts = 0; growthAttempts < ConfigurationData.SPEED_INCREASE_VALUE;
                 growthAttempts++) {
            	if(growthAttempts == 0 | canSustainPlant(world.getBlockState(pos), world, pos, null, (IPlantable) blockAbove))
            	{
            		nextPlantBlock
            		  .updateTick(world, pos.up(height), world.getBlockState(pos.up(height)), rand);
            	}
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
      blockAbove.updateTick(world, pos.up(), world.getBlockState(pos.up()), rand);
    }

  }

  private void checkFallable(World worldIn, BlockPos pos) {
    if (canFallThrough(worldIn.getBlockState(pos.down())) && pos.getY() >= 0) {
      int i = 32;

      if (!fallInstantly && worldIn.isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i))) {
        if (!worldIn.isRemote) {
          EntityFallingBlock
              entityfallingblock =
              new EntityFallingBlock(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY(),
                                     (double) pos.getZ() + 0.5D, worldIn.getBlockState(pos));
          onStartFalling(entityfallingblock);
          worldIn.spawnEntityInWorld(entityfallingblock);
        }
      } else {
        worldIn.setBlockToAir(pos);
        BlockPos blockpos;

        for (blockpos = pos.down(); canFallThrough(worldIn.getBlockState(pos.down())) && blockpos.getY() > 0;
             blockpos = blockpos.down()) {
          ;
        }

        if (blockpos.getY() > 0) {
          worldIn.setBlockState(blockpos.up(), getDefaultState());
        }
      }
    }
  }

  @Override
  public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable) {
    BlockPos plantPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
    Block plant = plantable.getPlant(world, plantPos).getBlock();
    EnumPlantType plantType = plantable.getPlantType(world, plantPos);

    switch (plantType) {
      case Desert: {
        return true;
      }
      case Water: {
        return world.getBlockState(pos).getBlock().getMaterial(world.getBlockState(pos)) == Material.WATER &&
               world.getBlockState(pos) == getDefaultState();
      }
      case Beach: {
        return (world.getBlockState(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ())).getBlock()
                    .getMaterial(world.getBlockState(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()))) == Material.WATER ||
                world.getBlockState(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ())).getBlock()
                    .getMaterial(world.getBlockState(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()))) == Material.WATER ||
                world.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1)).getBlock()
                    .getMaterial(world.getBlockState(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()))) == Material.WATER ||
                world.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1)).getBlock()
                    .getMaterial(world.getBlockState(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()))) == Material.WATER);
      }
    }

    return false;
  }

  @Override
  public String getSpecialName(ItemStack stack) {
    return stack.getItemDamage() == 0 ? "default" : "red";
  }

  public enum EnumType implements IStringSerializable {
    SAND(0, "snad", "default", MapColor.SAND),
    RED_SAND(1, "red_snad", "red", MapColor.ADOBE);

    private static final BlockSnad.EnumType[] META_LOOKUP = new BlockSnad.EnumType[values().length];
    private final int meta;
    private final String name;
    private final MapColor mapColor;
    private final String unlocalizedName;

    EnumType(int meta, String name, String unlocalizedName, MapColor mapColor) {
      this.meta = meta;
      this.name = name;
      this.mapColor = mapColor;
      this.unlocalizedName = unlocalizedName;
    }

    public int getMetadata() {
      return meta;
    }

    public String toString() {
      return name;
    }

    public MapColor getMapColor() {
      return mapColor;
    }

    public static BlockSnad.EnumType byMetadata(int meta) {
      if (meta < 0 || meta >= META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }

    public String getName() {
      return name;
    }

    public String getUnlocalizedName() {
      return unlocalizedName;
    }

    static {
      for (BlockSnad.EnumType blockType : values()) {
        META_LOOKUP[blockType.getMetadata()] = blockType;
      }
    }
  }
}
