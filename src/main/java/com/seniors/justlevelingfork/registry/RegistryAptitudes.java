package com.seniors.justlevelingfork.registry;

import com.seniors.justlevelingfork.JustLevelingFork;
import com.seniors.justlevelingfork.handler.HandlerResources;
import com.seniors.justlevelingfork.registry.aptitude.Aptitude;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RegistryAptitudes {

    private static final ResourceKey<Registry<Aptitude>> APTITUDES_KEY = ResourceKey.createRegistryKey(new ResourceLocation(JustLevelingFork.MOD_ID, "aptitudes"));
    private static final DeferredRegister<Aptitude> APTITUDES;
    public static Supplier<IForgeRegistry<Aptitude>> APTITUDES_REGISTRY;
    public static final RegistryObject<Aptitude> STRENGTH;
    public static final RegistryObject<Aptitude> CONSTITUTION;
    public static final RegistryObject<Aptitude> DEXTERITY;
    public static final RegistryObject<Aptitude> DEFENSE;
    public static final RegistryObject<Aptitude> INTELLIGENCE;
    public static final RegistryObject<Aptitude> BUILDING;
    public static final RegistryObject<Aptitude> MAGIC;
    public static final RegistryObject<Aptitude> LUCK;
    public static void load(IEventBus eventBus) {
        APTITUDES.register(eventBus);
    }

    private static Aptitude register(int index, String name, ResourceLocation[] lockedTexture, ResourceLocation background) {
        ResourceLocation key = new ResourceLocation(JustLevelingFork.MOD_ID, name);
        return new Aptitude(index, key, lockedTexture, background);
    }

    public static Aptitude getAptitude(String aptitudeName) {
        return APTITUDES_REGISTRY.get().getValues().stream().collect(Collectors.toMap(Aptitude::getName, Aptitude::get)).get(aptitudeName.toLowerCase());
    }

    static {
        APTITUDES = DeferredRegister.create(APTITUDES_KEY, JustLevelingFork.MOD_ID);
        APTITUDES_REGISTRY = APTITUDES.makeRegistry(Aptitude.class, () -> {
            return (new RegistryBuilder()).disableSaving();
        });
        STRENGTH = APTITUDES.register("strength", () -> {
            return register(0, "strength", HandlerResources.STRENGTH_LOCKED_ICON, new ResourceLocation("minecraft:textures/block/yellow_terracotta.png"));
        });
        CONSTITUTION = APTITUDES.register("constitution", () -> {
            return register(1, "constitution", HandlerResources.CONSTITUTION_LOCKED_ICON, new ResourceLocation("minecraft:textures/block/red_terracotta.png"));
        });
        DEXTERITY = APTITUDES.register("dexterity", () -> {
            return register(2, "dexterity", HandlerResources.DEXTERITY_LOCKED_ICON, new ResourceLocation("minecraft:textures/block/blue_terracotta.png"));
        });
        DEFENSE = APTITUDES.register("defense", () -> {
            return register(3, "defense", HandlerResources.DEFENSE_LOCKED_ICON, new ResourceLocation("minecraft:textures/block/cyan_terracotta.png"));
        });
        INTELLIGENCE = APTITUDES.register("intelligence", () -> {
            return register(4, "intelligence", HandlerResources.INTELLIGENCE_LOCKED_ICON, new ResourceLocation("minecraft:textures/block/orange_terracotta.png"));
        });
        BUILDING = APTITUDES.register("building", () -> {
            return register(5, "building", HandlerResources.BUILDING_LOCKED_ICON, new ResourceLocation("minecraft:textures/block/brown_terracotta.png"));
        });
        MAGIC = APTITUDES.register("magic", () -> {
            return register(6, "magic", HandlerResources.MAGIC_LOCKED_ICON, new ResourceLocation("minecraft:textures/block/purple_terracotta.png"));
        });
        LUCK = APTITUDES.register("luck", () -> {
            return register(7, "luck", HandlerResources.LUCK_LOCKED_ICON, new ResourceLocation("minecraft:textures/block/lime_terracotta.png"));
        });
    }
}


