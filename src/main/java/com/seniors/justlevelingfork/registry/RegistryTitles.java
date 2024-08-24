package com.seniors.justlevelingfork.registry;

import com.seniors.justlevelingfork.JustLevelingFork;
import com.seniors.justlevelingfork.common.capability.AptitudeCapability;
import com.seniors.justlevelingfork.config.models.TitleModel;
import com.seniors.justlevelingfork.handler.HandlerTitlesConfig;
import com.seniors.justlevelingfork.registry.title.Title;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RegistryTitles {
    public static final ResourceKey<Registry<Title>> TITLES_KEY = ResourceKey.createRegistryKey(new ResourceLocation(JustLevelingFork.MOD_ID, "titles"));
    public static final DeferredRegister<Title> TITLES;
    public static Supplier<IForgeRegistry<Title>> TITLES_REGISTRY;

    public static final RegistryObject<Title> TITLELESS;
    public static final RegistryObject<Title> ADMIN;

    public static void load(IEventBus eventBus) {
        HandlerTitlesConfig.HANDLER.instance().titleList.forEach(title -> {
            title.Registry(TITLES);
        });

        TITLES.register(eventBus);
    }

    private static Title register(String name, boolean requirement) {
        ResourceLocation key = new ResourceLocation(JustLevelingFork.MOD_ID, name);
        return new Title(key, requirement, true);
    }

    public static Title getTitle(String titleName) {
        return TITLES_REGISTRY.get().getValues().stream().collect(Collectors.toMap(Title::getName, Title::get)).get(titleName);
    }

    public static void syncTitles(ServerPlayer serverPlayer) {
        serverPlayerTitles(serverPlayer);
        serverPlayer.getCapability(RegistryCapabilities.APTITUDE).ifPresent(aptitudeCapability -> {
            Title title = getTitle(AptitudeCapability.get(serverPlayer).getPlayerTitle());
            if (title != null) {
                serverPlayer.setCustomName(new TranslatableComponent(title.getKey()));
            }
        });
    }

    public static void serverPlayerTitles(ServerPlayer serverPlayer) {
        if (!serverPlayer.isDeadOrDying())
            serverPlayer.getCapability(RegistryCapabilities.APTITUDE).ifPresent(capability -> {
                for (TitleModel titleModel : HandlerTitlesConfig.HANDLER.instance().titleList) {
                    titleModel.getTitle().setRequirement(serverPlayer, titleModel.CheckRequirements(serverPlayer));
                }
                ADMIN.get().setRequirement(serverPlayer, serverPlayer.hasPermissions(2));
            });
    }

    static {
        TITLES = DeferredRegister.create(TITLES_KEY, JustLevelingFork.MOD_ID);
        TITLES_REGISTRY = TITLES.makeRegistry(Title.class, () -> {
            return new RegistryBuilder().disableSaving();
        });
        TITLELESS = TITLES.register("titleless", () -> {
            return register("titleless", true);
        });
        ADMIN = TITLES.register("administrator", () -> {
            return register("administrator", false);
        });
    }
}


