package com.neecass.neecats;

import com.neecass.neecats.items.ModItems;
import com.neecass.neecats.tabs.CreativeTabNeeCats;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
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



    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("preinit");

        tabNeeCats = new CreativeTabNeeCats(CreativeTabs.getNextID(), "NeeCats");
        ModItems.init();
        PacketHandler.simpleNetworkWrapper.registerMessage(MessageCastCatWand.Handler.class, MessageCastCatWand.class, 1, Side.SERVER);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("init");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("postinit");
    }

    public static CreativeTabNeeCats tabNeeCats;


}
