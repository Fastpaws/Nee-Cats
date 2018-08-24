package com.neecass.neecats.items;

import com.neecass.neecats.NeeCats;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = NeeCats.MODID)
public class ModItems {

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBase("test"));
        event.getRegistry().register(new ItemBase("test_two"));
        event.getRegistry().register(new ItemCatWand());
    }

}
