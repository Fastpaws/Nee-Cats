package com.neecass.neecats;


import com.neecass.neecats.items.ItemCatWand;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

public class MessageCastCatWand implements IMessage {

    private int entityId;

    public MessageCastCatWand() {
        // need this constructor
    }

    public MessageCastCatWand(int parEntityId) {
        this.entityId = parEntityId;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
    }



    public static class Handler implements IMessageHandler<MessageCastCatWand, IMessage> {

        @Override
        public IMessage onMessage(MessageCastCatWand message, MessageContext ctx) {

            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
            WorldServer serverWorld = serverPlayer.getServerWorld();
            int targetEntityId = message.entityId;

            // get out of network thread
            serverWorld.addScheduledTask(() -> {

                if(serverPlayer.getHeldItemMainhand().getItem().getClass() == ItemCatWand.class) {
                    Entity targetEntity = serverWorld.getEntityByID(targetEntityId);
                    ITextComponent msg = new TextComponentString("no target");
                    if (targetEntity instanceof EntityLivingBase && !targetEntity.isDead) {
                        targetEntity.setFire(1);
                        msg = new TextComponentString("target: " + targetEntity.getDisplayName());

                        // get all tamed cats nearby
                        AxisAlignedBB aabb = new AxisAlignedBB(serverPlayer.getPosition());
                        aabb.grow(10);
                        List<EntityOcelot> cats = serverWorld.getEntitiesWithinAABB(EntityOcelot.class, aabb);

                        // send them to attack target
                        cats.forEach(cat -> cat.setRevengeTarget((EntityLivingBase) targetEntity));
                        // cats.forEach(cat->cat.setFire(1)); // animals were harmed in the testing of this mod

                    }
                    serverWorld.getMinecraftServer().sendMessage(msg);
                } else {
                    serverWorld.getMinecraftServer().sendMessage(new TextComponentString("Cheater! You aren't holding a cat wand!"));
                }

            });

            return null; // no response message
        }
    }
}