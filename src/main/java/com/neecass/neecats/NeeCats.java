package com.neecass.neecats;

import com.neecass.neecats.items.ItemCatWand;
import com.neecass.neecats.proxy.CommonProxy;
import com.neecass.neecats.tabs.CreativeTabNeeCats;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = NeeCats.MODID, name = NeeCats.NAME, version = NeeCats.VERSION)
public class NeeCats
{
    public static final String MODID = "neecats";
    public static final String NAME = "Nee Cats";
    public static final String VERSION = "1.0";

    private static Logger logger;

    @Mod.Instance
    public static NeeCats instance;

    @SidedProxy(clientSide = "com.neecass.neecats.proxy.ClientProxy", serverSide = "com.neecass.neecats.proxy.CommonProxy")
    public static CommonProxy proxy;


    public static CreativeTabNeeCats tabNeeCats;



    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();

        tabNeeCats = new CreativeTabNeeCats(CreativeTabs.getNextID(), "NeeCats");

        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        // some example code
        logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        proxy.postInit(event);
    }
}
