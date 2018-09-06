package com.neecass.neecats.network;


import com.neecass.neecats.NeeCats;
import com.neecass.neecats.items.ItemCatWand;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public class MessageCatsAttack implements IMessage {

    private int entityId;

    public MessageCatsAttack() {
        // need this constructor
    }

    public MessageCatsAttack(int parEntityId) {
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



    public static class Handler implements IMessageHandler<MessageCatsAttack, IMessage> {


        @Override
        public IMessage onMessage(MessageCatsAttack message, MessageContext ctx) {

            if (ctx.side == Side.CLIENT || message.entityId == 0) {
                return null;
            }

            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
            WorldServer serverWorld = serverPlayer.getServerWorld();

            // send to main server thread. Network threads can't work with some/many/most(?) objects
            serverWorld.addScheduledTask(() -> {

                Entity targetEntity = serverWorld.getEntityByID(message.entityId);

                if (!(targetEntity instanceof EntityLivingBase)) {
                    serverPlayer.sendStatusMessage(new TextComponentString("Target not a living entity."), false);
                    return;
                }

                if (serverPlayer.getHeldItemMainhand().getItem().getClass() != ItemCatWand.class) {
                    serverPlayer.sendStatusMessage(new TextComponentString("Cheater! You are not holding a cat wand!"), false);
                    return;
                }

                if (targetEntity.isOnSameTeam(serverPlayer)) {
                    serverPlayer.sendStatusMessage(new TextComponentString("Target is on your team."), false);
                    return;
                }

                serverPlayer.sendStatusMessage(new TextComponentString("Target: " + targetEntity.getName()), false);
                //targetEntity.setFire(1);

                // get all tamed cats nearby
                AxisAlignedBB aabb = new AxisAlignedBB(serverPlayer.getPosition()).grow(50D);
                List<EntityOcelot> cats = serverWorld.<EntityOcelot>getEntitiesWithinAABB(EntityOcelot.class, aabb);
                for (EntityOcelot cat : cats) {
                    // attack!
                    cat.setAttackTarget((EntityLivingBase) targetEntity);
                }

            });

            return null; // no response message
        }
    }
}