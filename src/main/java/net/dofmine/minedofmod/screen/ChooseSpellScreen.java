package net.dofmine.minedofmod.screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChooseSpellScreen extends Screen {

    public ChooseSpellScreen(TextComponent textComponent) {
        super(textComponent);
    }

}
