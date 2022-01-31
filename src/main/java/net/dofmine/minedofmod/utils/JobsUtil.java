package net.dofmine.minedofmod.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class JobsUtil {

    private JobsUtil() {}

    public static List<ICapabilityProvider> getAllCapabilities(Player player) {
        List<ICapabilityProvider> lst = new ArrayList<>();
        try {
            Method method = player.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getCapabilities");
            method.setAccessible(true);
            CapabilityDispatcher capabilityDispatcher = (CapabilityDispatcher) method.invoke(player);
            Field field = capabilityDispatcher.getClass().getDeclaredField("caps");
            field.setAccessible(true);
            lst = List.of((ICapabilityProvider[]) field.get(capabilityDispatcher));
        } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return lst;
    }

    public static List<ICapabilityProvider> getAllCapabilitiesForLocalPlayer(Player player) {
        List<ICapabilityProvider> lst = new ArrayList<>();
        try {
            Method method = player.getClass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getCapabilities");
            method.setAccessible(true);
            CapabilityDispatcher capabilityDispatcher = (CapabilityDispatcher) method.invoke(player);
            Field field = capabilityDispatcher.getClass().getDeclaredField("caps");
            field.setAccessible(true);
            lst = List.of((ICapabilityProvider[]) field.get(capabilityDispatcher));
        } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return lst;
    }

}
