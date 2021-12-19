package net.dofmine.minedofmod.items.key;

import net.dofmine.minedofmod.block.custom.SpecialDoorTeleportate;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.tabs.ModCreativeTabs;
import net.dofmine.minedofmod.utils.FilesUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.dimension.DimensionDefaults;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraft.world.level.storage.WorldData;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TeleportateKey extends Item {

    private static List<SpecialDoorTeleportate> doorTeleportates;
    private static String filePath;

    public TeleportateKey() {
        super(new Properties().stacksTo(1).tab(ModCreativeTabs.MODS_TABS));
        doorTeleportates = new ArrayList<>();
        filePath = "./teleportKey.txt";
    }

    public void addDoorTeleportate(SpecialDoorTeleportate specialDoorTeleportate) {
        doorTeleportates.add(specialDoorTeleportate);
        List<String> doorList = FilesUtils.lireFichier(filePath);
        doorList.add(String.format("%d;%d;%d", specialDoorTeleportate.getBlockPos().getX(), specialDoorTeleportate.getBlockPos().getY(), specialDoorTeleportate.getBlockPos().getZ()));
        FilesUtils.ecrireUnfichier(doorList, filePath);
    }

    public List<SpecialDoorTeleportate> getDoorTeleportates() {
        return doorTeleportates;
    }

    public boolean isPresent(SpecialDoorTeleportate specialDoorTeleportate) {
        for (SpecialDoorTeleportate sdt : doorTeleportates) {
            if ((sdt.getBlockPos().getX() == specialDoorTeleportate.getBlockPos().getX() && sdt.getBlockPos().getY() == specialDoorTeleportate.getBlockPos().getY()
                    && sdt.getBlockPos().getZ() == specialDoorTeleportate.getBlockPos().getZ()) ||
                    ((sdt.getBlockPos().getX() - specialDoorTeleportate.getBlockPos().getX() <= 1 && sdt.getBlockPos().getX() - specialDoorTeleportate.getBlockPos().getX() >= -1)
                            && (sdt.getBlockPos().getY() - specialDoorTeleportate.getBlockPos().getY() <= 1 && sdt.getBlockPos().getY() - specialDoorTeleportate.getBlockPos().getY() >= -1)
                            && (sdt.getBlockPos().getZ() - specialDoorTeleportate.getBlockPos().getZ() <= 1 && sdt.getBlockPos().getZ() - specialDoorTeleportate.getBlockPos().getZ() >= -1))) {
                return true;
            }
        }
        return false;
    }

    public static void resolveTeleportKeyComponents(ItemStack itemStack, @Nullable CommandSourceStack commandSourceStack, @Nullable Player player) {
        if (doorTeleportates.isEmpty()) {
            for (String str : FilesUtils.lireFichier(filePath)) {
                String[] tabs = str.split(";");
                doorTeleportates.add(new SpecialDoorTeleportate(new BlockPos(Integer.parseInt(tabs[0]), Integer.parseInt(tabs[1]), Integer.parseInt(tabs[2]))));
            }
        }
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

}
