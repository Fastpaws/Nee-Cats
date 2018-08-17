package com.neecass.neecats.items;

import com.neecass.neecats.NeeCats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;


public class ItemBase extends Item {

    public ItemBase(String name) {

        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(NeeCats.tabNeeCats);

    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        playerIn.jump();
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

}
