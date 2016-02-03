package com.robrit.snad.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * Created by Tim on 2/3/2016.
 */
public class ItemBlockSnadMeta extends ItemBlock {

  public ItemBlockSnadMeta(Block block) {
    super(block);
    if (!(block instanceof IMetaBlockSnad)) {
      throw new IllegalArgumentException(String.format(
          "The given Block %s is not an instance of ISpecialBlockName!",
          block.getUnlocalizedName()));
    }
    this.setMaxDamage(0);
    this.setHasSubtypes(true);
  }

  public int getMetadata(int damage) {
    return damage;
  }

  @Override
  public String getUnlocalizedName(ItemStack stack) {
    return super.getUnlocalizedName(stack) + "." + ((IMetaBlockSnad) this.block)
        .getSpecialName(stack);
  }
}
