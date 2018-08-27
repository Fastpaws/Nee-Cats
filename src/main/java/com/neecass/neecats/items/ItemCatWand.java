package com.neecass.neecats.items;

import com.neecass.neecats.MessageCastCatWand;
import com.neecass.neecats.NeeCats;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.neecass.neecats.PacketHandler.simpleNetworkWrapper;


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



        if(!playerIn.isSneaking()) {
            // NOT SNEAKING

            playerIn.jump();
            //simpleNetworkWrapper.sendToServer(new MessageCastCatWand(playerIn.getEntityId()));

        } else {
            // SNEAKING
            // spawn a tamed ocelot (cat)

            if(!worldIn.isRemote) {

                // cats dont like spawning in walls
                java.util.Random rand = new java.util.Random();
                double posX, posY, posZ;
                boolean validSpawn = false;
                int attempts = 0;
                do {
                    posX = playerIn.posX + rand.nextInt(5) - 2;
                    posY = playerIn.posY;
                    posZ = playerIn.posZ + rand.nextInt(5) - 2;
                    validSpawn = worldIn.isAirBlock(new BlockPos(posX, posY, posZ));
                    attempts++;
                } while (!validSpawn && attempts < 10);

                if (validSpawn) {
                    EntityOcelot ocelot = new EntityOcelot(worldIn);
                    ocelot.setLocationAndAngles(posX, posY, posZ, playerIn.getPitchYaw().x, 0F);
                    ocelot.spawnExplosionParticle();
                    ocelot.enablePersistence();
                    ocelot.setTamed(true);
                    ocelot.setTamedBy(playerIn);
                    worldIn.spawnEntity(ocelot);
                }
            }
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));

    }
}
