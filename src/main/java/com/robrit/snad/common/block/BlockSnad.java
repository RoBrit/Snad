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

import com.robrit.snad.common.item.IMetaBlockSnad;
import com.robrit.snad.common.item.ItemBlockSnadMeta;
import com.robrit.snad.common.util.ConfigurationData;
import com.robrit.snad.common.util.ModInformation;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockReed;
import net.minecraft.block.SoundType;
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
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockSnad extends BlockFalling implements IMetaBlockSnad {

  public static final PropertyEnum<BlockSnad.EnumType> VARIANT =
      PropertyEnum.create("variant", BlockSnad.EnumType.class);
  private static final String BLOCK_IDENTIFIER = "snad";

  public BlockSnad() {
    super(Material.SAND);
    setTickRandomly(true);
    setHardness(0.5F);
    setSoundType(SoundType.SAND);
    setCreativeTab(CreativeTabs.MISC);
    setUnlocalizedName(BLOCK_IDENTIFIER);
    setDefaultState(blockState.getBaseState().withProperty(VARIANT, EnumType.SNAD));

    setRegistryName(new ResourceLocation(ModInformation.MOD_ID, BLOCK_IDENTIFIER));
  }

  @Override
  public int damageDropped(IBlockState state) {
    return state.getValue(VARIANT).getMetadata();
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
    for (BlockSnad.EnumType blockType : BlockSnad.EnumType.values()) {
      list.add(new ItemStack(this, 1, blockType.getMetadata()));
    }
  }

  @Override
  @Deprecated
  public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
    return state.getValue(VARIANT).getMapColor();
  }

  @Override
  @Deprecated
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(VARIANT, BlockSnad.EnumType.byMetadata(meta));
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(VARIANT).getMetadata();
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, VARIANT);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public int getDustColor(IBlockState state) {
    return state.getValue(VARIANT).getDustColor();
  }

  @Override
  public ItemStack getPickBlock(IBlockState state, RayTraceResult target,
                                World world, BlockPos pos, EntityPlayer player) {
    return new ItemStack(Item.getItemFromBlock(this), 1,
                         getMetaFromState(world.getBlockState(pos)));
  }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
    if (!world.isRemote) {
      checkFallable(world, pos);
    }

    Block blockAbove = world.getBlockState(pos.up()).getBlock();

    if (blockAbove instanceof BlockReed || blockAbove instanceof BlockCactus) {
      boolean isSameBlockType = true;
      int height = 1;

      while (isSameBlockType) {
        if (world.getBlockState(pos.up(height)).getBlock() != null) {
          Block nextPlantBlock = world.getBlockState(pos.up(height)).getBlock();
          if (nextPlantBlock.getClass() == blockAbove.getClass()) {
            for (int growthAttempts = 0; growthAttempts < ConfigurationData.SPEED_INCREASE_VALUE;
                 growthAttempts++) {
              if (growthAttempts == 0 | canSustainPlant(world.getBlockState(pos), world, pos, null,
                                                        (IPlantable) blockAbove)) {
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
    if ((worldIn.isAirBlock(pos.down()) ||
         canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0) {
      if (!fallInstantly && worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {
        if (!worldIn.isRemote) {
          final EntityFallingBlock entityfallingblock =
              new EntityFallingBlock(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY(),
                                     (double) pos.getZ() + 0.5D, worldIn.getBlockState(pos));
          onStartFalling(entityfallingblock);
          worldIn.spawnEntity(entityfallingblock);
        }
      } else {
        IBlockState state = worldIn.getBlockState(pos);
        worldIn.setBlockToAir(pos);
        BlockPos blockpos;

        for (blockpos = pos.down();
             (worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos)))
             && blockpos.getY() > 0; blockpos = blockpos.down()) {
          //NOOP
        }

        if (blockpos.getY() > 0) {
          worldIn.setBlockState(blockpos.up(), state);
        }
      }
    }
  }

  @Override
  public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos,
                                 EnumFacing direction, IPlantable plantable) {
    final BlockPos plantPos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
    final EnumPlantType plantType = plantable.getPlantType(world, plantPos);

    switch (plantType) {
      case Desert: {
        return true;
      }
      case Water: {
        return world.getBlockState(pos).getMaterial() == Material.WATER &&
               world.getBlockState(pos) == getDefaultState();
      }
      case Beach: {
        return (
            (world.getBlockState(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ())).getMaterial()
             == Material.WATER) ||
            (world.getBlockState(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ())).getMaterial()
             == Material.WATER) ||
            (world.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1)).getMaterial()
             == Material.WATER) ||
            (world.getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1)).getMaterial()
             == Material.WATER));
      }
    }

    return false;
  }

  @Override
  public String getSpecialName(ItemStack stack) {
    return stack.getItemDamage() == 0 ? "default" : "red";
  }

  public enum EnumType implements IStringSerializable {
    SNAD(0, "snad", "default", MapColor.SAND, -2370656),
    RED_SNAD(1, "red_snad", "red", MapColor.ADOBE, -5679071);

    private static final BlockSnad.EnumType[] META_LOOKUP = new BlockSnad.EnumType[values().length];

    static {
      for (BlockSnad.EnumType blockType : values()) {
        META_LOOKUP[blockType.getMetadata()] = blockType;
      }
    }

    private final int meta;
    private final String name;
    private final MapColor mapColor;
    private final String unlocalizedName;
    private final int dustColor;

    EnumType(int meta, String name, String unlocalizedName, MapColor mapColor, int dustColor) {
      this.meta = meta;
      this.name = name;
      this.unlocalizedName = unlocalizedName;
      this.mapColor = mapColor;
      this.dustColor = dustColor;
    }

    public static BlockSnad.EnumType byMetadata(int meta) {
      if (meta < 0 || meta >= META_LOOKUP.length) {
        meta = 0;
      }

      return META_LOOKUP[meta];
    }

    @SideOnly(Side.CLIENT)
    public int getDustColor() {
      return this.dustColor;
    }

    public int getMetadata() {
      return this.meta;
    }

    public String toString() {
      return this.name;
    }

    public MapColor getMapColor() {
      return this.mapColor;
    }

    @Override
    public String getName() {
      return name;
    }

    public String getUnlocalizedName() {
      return unlocalizedName;
    }
  }
}
