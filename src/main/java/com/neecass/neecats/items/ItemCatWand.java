package com.neecass.neecats.items;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.neecass.neecats.network.MessageCatsAttack;
import com.neecass.neecats.NeeCats;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.neecass.neecats.network.PacketHandler.simpleNetworkWrapper;


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
            // send all owned tamed cats to attack your target!

            if(worldIn.isRemote) {

                //from http://www.minecraftforge.net/forum/topic/60098-112-give-potion-effect-to-looking-entity/
                //TODO make this less shit
                Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
                Entity pointedEntity = null;
                if (entity != null) {
                    if (Minecraft.getMinecraft().world != null) {

                        double range = (double) 50;
                        Minecraft.getMinecraft().objectMouseOver = entity.rayTrace(range, 20);
                        Vec3d vec3d = entity.getPositionEyes(20);
                        boolean flag = false;
                        int i = 3;                        double d1 = range;


                        if (Minecraft.getMinecraft().objectMouseOver != null) {
                            d1 = Minecraft.getMinecraft().objectMouseOver.hitVec.distanceTo(vec3d);
                        }
                        Vec3d vec3d1 = entity.getLook(1.0F);
                        Vec3d vec3d2 = vec3d.addVector(vec3d1.x * range, vec3d1.y * range, vec3d1.z * range);
                        pointedEntity = null;
                        Vec3d vec3d3 = null;
                        float f = 1.0F;
                        List<Entity> list = Minecraft.getMinecraft().world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(vec3d1.x * range, vec3d1.y * range, vec3d1.z * range).grow(1.0D, 1.0D, 1.0D), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
                            public boolean apply(@Nullable Entity p_apply_1_) {
                                return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
                            }
                        }));
                        double d2 = d1;
                        for (int j = 0; j < list.size(); ++j) {
                            Entity entity1 = list.get(j);
                            AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow((double) entity1.getCollisionBorderSize());
                            RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, vec3d2);
                            if (axisalignedbb.contains(vec3d)) {
                                if (d2 >= 0.0D) {
                                    pointedEntity = entity1;
                                    vec3d3 = raytraceresult == null ? vec3d : raytraceresult.hitVec;
                                    d2 = 0.0D;
                                }
                            } else if (raytraceresult != null) {
                                double d3 = vec3d.distanceTo(raytraceresult.hitVec);
                                if (d3 < d2 || d2 == 0.0D) {
                                    if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity() && !entity1.canRiderInteract()) {
                                        if (d2 == 0.0D) {
                                            pointedEntity = entity1;
                                            vec3d3 = raytraceresult.hitVec;
                                        }
                                    } else {
                                        pointedEntity = entity1;
                                        vec3d3 = raytraceresult.hitVec;
                                        d2 = d3;
                                    }
                                }
                            }
                        }
                        if (pointedEntity != null && flag && vec3d.distanceTo(vec3d3) > 3.0D) {
                            pointedEntity = null;
                            Minecraft.getMinecraft().objectMouseOver = new RayTraceResult(RayTraceResult.Type.MISS, vec3d3, (EnumFacing) null, new BlockPos(vec3d3));
                        }
                        if (pointedEntity != null && (d2 < d1 || Minecraft.getMinecraft().objectMouseOver == null)) {
                            Minecraft.getMinecraft().objectMouseOver = new RayTraceResult(pointedEntity, vec3d3);
                            if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                                Minecraft.getMinecraft().pointedEntity = pointedEntity;
                            }
                        }
                    }
                }
                // instruct server to run MessageCatsAttack on target entity
                if(pointedEntity != null) {
                    simpleNetworkWrapper.sendToServer(new MessageCatsAttack(pointedEntity.getEntityId()));
                }
            }

        } else {

            // SNEAKING
            // spawn a tamed ocelot (cat)
            if(!worldIn.isRemote) {
                // spawn on block player is looking at
                RayTraceResult ray = playerIn.rayTrace(10F, 1F);
                if (ray.typeOfHit == RayTraceResult.Type.BLOCK) {
                    BlockPos spawnPos = ray.getBlockPos().offset(ray.sideHit);
                    spawnTamedOcelot(playerIn, spawnPos);
                }
            }
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }


    public void spawnTamedOcelot(EntityPlayer player, BlockPos pos) {
        if(!player.world.isRemote && !player.world.getBlockState(pos).causesSuffocation()) {
            EntityOcelot ocelot = new EntityOcelot(player.world);
            ocelot.setLocationAndAngles(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5, 1 - player.rotationYaw, 0F);
            ocelot.setTamedBy(player);
            ocelot.setTameSkin(itemRand.nextInt(3)+1);
            player.world.spawnEntity(ocelot);
            ocelot.spawnExplosionParticle();
        }
    }

}
