package com.neecass.neecats.items;

import com.neecass.neecats.NeeCats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;


public class ItemCatWand extends Item {

    public ItemCatWand() {

        setUnlocalizedName("cat_wand");
        setCreativeTab(NeeCats.tabNeeCats);
        setRegistryName("cat_wand");

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        playerIn.jump();
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean isDamageable() {
        return false;
    }
}
