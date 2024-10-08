package dev.risas.module.impl.ghost;

import dev.risas.Rise;
import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import net.minecraft.client.gui.GuiMainMenu;
import org.lwjgl.opengl.Display;

@ModuleInfo(name = "SelfDestruct", description = "Makes Rise look like normal Minecraft when enabled", category = Category.LEGIT)
public final class SSMode extends Module {

    @Override
    protected void onEnable() {
        Display.setTitle("Minecraft 1.8.9");
        mc.resetWindowIcon();
        Rise.INSTANCE.guiMainMenu = new GuiMainMenu();
        mc.displayGuiScreen(null);
        Rise.INSTANCE.destructed = true;
        this.setEnabled(false);
    }
}
