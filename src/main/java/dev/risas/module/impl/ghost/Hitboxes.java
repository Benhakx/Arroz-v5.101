package dev.risas.module.impl.ghost;

import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import dev.risas.setting.impl.BooleanSetting;
import dev.risas.setting.impl.NumberSetting;

@ModuleInfo(name = "Hitboxes", description = "Expands entity hitboxes", category = Category.LEGIT)
public class Hitboxes extends Module {

    public NumberSetting expand = new NumberSetting("Expand", this, 1, 0, 5, 0.05);
    public BooleanSetting invis = new BooleanSetting("Expand Invis", this, false);

}
