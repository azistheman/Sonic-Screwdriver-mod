package net.aj.sonicscrewdriver.item;

import net.aj.sonicscrewdriver.SonicScrewdriver;
import net.aj.sonicscrewdriver.item.custom.SonicScrewdriverItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModsItems {
    //public static final Item SONIC_SCREWDRIVER = registerItem("sonic_screwdriver", new Item(new Item.Settings()));
    public static final Item SONIC_SCREWDRIVER = registerItem("sonic_screwdriver", new SonicScrewdriverItem(new Item.Settings()));


    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(SonicScrewdriver.MOD_ID, name), item);
    }

    public static void registerModItems() {
        SonicScrewdriver.LOGGER.info("Registering Mod Items for " + SonicScrewdriver.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            entries.add(SONIC_SCREWDRIVER);

        });
    }
}
