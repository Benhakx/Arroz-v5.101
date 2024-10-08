package dev.risas.setting.impl;

import dev.risas.module.Module;
import dev.risas.setting.Setting;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class NoteSetting extends Setting {

    public NoteSetting(final String note, final Module parent) {
        this.name = note;
        parent.settings.add(this);
    }

}
