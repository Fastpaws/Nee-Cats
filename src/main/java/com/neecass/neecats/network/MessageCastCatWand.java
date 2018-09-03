package com.neecass.neecats.network;


import com.neecass.neecats.NeeCats;
import com.neecass.neecats.items.ItemCatWand;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

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

            if (ctx.side == Side.CLIENT || message.entityId == 0) {
                return null;
            }

            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
            WorldServer serverWorld = serverPlayer.getServerWorld();

            // send to main server thread. Network threads can't work with some/many/most(?) objects
            serverWorld.addScheduledTask(() -> {

                Entity targetEntity = serverWorld.getEntityByID(message.entityId);

                if (serverPlayer.getHeldItemMainhand().getItem().getClass() != ItemCatWand.class) {
                    serverPlayer.sendStatusMessage(new TextComponentString("Cheater! You are not holding a cat wand!"), false);
                } else {

                    if (targetEntity instanceof EntityOcelot) {
                        if (((EntityOcelot) targetEntity).getOwnerId() == serverPlayer.getUniqueID()) {
                            serverPlayer.sendStatusMessage(new TextComponentString("Can't target your own tamed cats."), false);
                            return;
                        }
                    }

                    if (targetEntity instanceof EntityLivingBase && targetEntity.isEntityAlive() && targetEntity.getUniqueID() != serverPlayer.getUniqueID())
                    {
                        serverPlayer.sendStatusMessage(new TextComponentString("target: " + targetEntity.getName()), true);
                        targetEntity.setFire(1);

                        // get all tamed cats nearby
                        AxisAlignedBB aabb = new AxisAlignedBB(serverPlayer.getPosition()).grow(10D);
                        NeeCats.logger.info(aabb.toString());
                        List<EntityOcelot> cats = serverWorld.<EntityOcelot>getEntitiesWithinAABB(EntityOcelot.class, aabb);
                        NeeCats.logger.info("number of cats: " + cats.size());

                        if (!cats.isEmpty()) {
                            NeeCats.logger.info("list of cats:");
                            for (EntityOcelot cat : cats) {
                                NeeCats.logger.info(cat.toString());
                                cat.spawnExplosionParticle();
                                cat.setFire(3);
                            }
                        }

                    }
                }

                serverPlayer.sendMessage(new TextComponentString("hello world - from serverPlayer::sendMessage"));
                serverPlayer.sendStatusMessage(new TextComponentString("hello world - from serverPlayer::sendStatusMessage"), false);

            });

            return null; // no response message
        }


    }
}