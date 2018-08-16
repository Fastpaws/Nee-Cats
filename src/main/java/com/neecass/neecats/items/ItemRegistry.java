package com.neecass.neecats.items;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ItemRegistry {

    @SubscribeEvent
    public static void registerItemBlocks(Register<Item> event) {
        event.getRegistry().register(new ItemCatWand());
    }

}