package com.neecass.neecats.items;

import com.neecass.neecats.NeeCats;
import net.minecraft.item.Item;


public class ItemBase extends Item {

    public ItemBase() {}

    public ItemBase(String name) {

        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(NeeCats.tabNeeCats);
    }

}
