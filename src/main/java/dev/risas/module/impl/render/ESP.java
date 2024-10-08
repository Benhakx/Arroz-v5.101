/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.risas.module.impl.render;

import dev.risas.module.Module;
import dev.risas.module.api.ModuleInfo;
import dev.risas.module.enums.Category;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

@Exclude({Strategy.NUMBER_OBFUSCATION, Strategy.FLOW_OBFUSCATION, Strategy.NAME_REMAPPING, Strategy.REFERENCE_OBFUSCATION, Strategy.DEBUG_STRIPPING})
@ModuleInfo(name = "ESP", description = "Renders a box around players through walls", category = Category.RENDER)
public final class ESP extends Module {
}