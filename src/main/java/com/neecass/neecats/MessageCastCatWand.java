package com.neecass.neecats;


import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
            int targetEntityId = message.entityId;
            serverPlayer.getServerWorld().addScheduledTask(() -> {
                Entity target = serverPlayer.getServerWorld().getEntityByID(targetEntityId);
                //target.setFire(2);
            });

            return null; // no response message
        }
    }
}