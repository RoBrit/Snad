/*
 * ItemBlockSnadMeta.java
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

package com.robrit.snad.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockSnadMeta extends ItemBlock {

  public ItemBlockSnadMeta(Block block) {
    super(block);
    if (!(block instanceof IMetaBlockSnad)) {
      throw new IllegalArgumentException(String.format(
          "The given Block %s is not an instance of ISpecialBlockName!",
          block.getUnlocalizedName()));
    }
    setMaxDamage(0);
    setHasSubtypes(true);
  }

  public int getMetadata(int damage) {
    return damage;
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {
    return super.getUnlocalizedName(stack) + "." + ((IMetaBlockSnad) block).getSpecialName(stack);
  }
}
