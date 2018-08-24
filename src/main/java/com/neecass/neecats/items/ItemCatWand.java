package com.neecass.neecats.items;

import com.neecass.neecats.MessageCastCatWand;
import com.neecass.neecats.NeeCats;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import static com.neecass.neecats.NeeCatsPacketHandler.simpleNetworkWrapper;


public class ItemCatWand extends ItemBase {

    public ItemCatWand() {
        setRegistryName("cat_wand");
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(NeeCats.tabNeeCats);
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        //playerIn.setFire(5);
        simpleNetworkWrapper.sendToServer(new MessageCastCatWand(playerIn.getEntityId()));

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
