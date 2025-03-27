package com.guhcat.raven.rge.client.datagen;

import com.guhcat.raven.rge.datagen.EnchantmentGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class RgeDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator){
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(EnchantmentGenerator::new);
    }
}
