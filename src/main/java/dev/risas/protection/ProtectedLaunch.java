package dev.risas.protection;

import dev.risas.Rise;
import dev.risas.command.Command;
import dev.risas.command.CommandManager;
import dev.risas.command.impl.PlayerHacks;
import dev.risas.command.impl.*;
import dev.risas.creative.RiseTab;
import dev.risas.module.Module;
import dev.risas.module.impl.combat.*;
import dev.risas.module.impl.ghost.*;
import dev.risas.module.impl.movement.*;
import dev.risas.module.impl.other.Sniper;
import dev.risas.module.impl.other.Spammer;
import dev.risas.module.impl.other.*;
import dev.risas.module.impl.player.*;
import dev.risas.module.impl.render.*;
import dev.risas.module.impl.render.particles.Particles;
import dev.risas.module.impl.render.targethud.TargetHud;
import dev.risas.module.manager.ModuleManager;
import dev.risas.notifications.NotificationManager;
import dev.risas.script.ScriptManager;
import dev.risas.ui.altmanager2.alt.GuiAltLogin;
import dev.risas.ui.auth.AuthGUI;
import dev.risas.ui.clickgui.impl.ClickGUI;
import dev.risas.ui.clickgui.impl.strikeless.StrikeGUI;
import dev.risas.ui.guitheme.GuiTheme;
import dev.risas.ui.mainmenu.MainMenu;
import dev.risas.util.misc.FileUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.main.Main;
import store.intent.api.account.IntentAccount;
import store.intent.intentguard.annotation.Bootstrap;
import store.intent.intentguard.annotation.Native;

import java.io.File;

@Native
public class ProtectedLaunch {

    @Native
    @Bootstrap
    public static void start() {
        if (Main.apiKey != null && !Main.apiKey.isEmpty())
            Rise.intentAccount = new IntentAccount();

        if (Rise.intentAccount == null) Rise.intentAccount = new IntentAccount();

        final Rise instance = Rise.INSTANCE;
        //ircClient = new IRCClient();
        try {
            (instance.moduleManager = new ModuleManager()).moduleList = new Module[]{
                    // IMPORTANT RENDER
                    new Interface(),
                    new TabGui(),

                    // COMBAT
                    new AutoGap(),
                    new AutoHead(),
                    new Aura(),
                    new FastBow(),
                    new TPAura(),
                    new BackTrack(),
                    new Velocity(),
                    new TargetStrafe(),
                    new FastBow(),
                    new AutoSoup(),
                    new FightBot(),
                    new AntiBot(),
                    new WTap(),
                    new Criticals(),
                    new ComboOneHit(),
                    new Regen(),
                    new AttackCrash(),

                    // PLAYER
                    new Derp(),
                    new ChestAura(),
                    new Stealer(),
                    new AutoTool(),
                    new FastPlace(),
                    new FastEat(),
                    new NoVoid(),
                    new NoFall(),
                    new Scaffold(),
                    new FastBreak(),
                    new Manager(),
                    new Infinite(),

                    // MOVEMENT
                    new Sprint(),
                    new Sneak(),
                    new ResetVL(),
                    new Fly(),
                    new Speed(),
                    new No003(),
                    new Strafe(),
                    new Jesus(),
                    new NoSlow(),
                    new HighJump(),
                    new LongJump(),
                    new InvMove(),
                    new Step(),
                    new Phase(),
                    new Blink(),
                    new Spider(),
                    new ClickTP(),
                    new BedWalker(),
                    new BowFly(),
                    new Clipper(),
                    new Terrain(),
                    new SafeWalk(),
                    new NoBob(),

                    // GHOST
                    new AimAssist(),
                    new Reach(),
                    new AutoClicker(),
                    new LegitScaffold(),
                    new KeepSprint(),
                    new Hitboxes(),
                    new Eagle(),
                    new NoClickDelay(),
                    new Keystrokes(),
                    new Freelook(),

                    // OTHER
                    new AutoHypixel(),
                    new Disabler(),
                    new DisablerOld(),
                    new Plugins(),
                    new AutoAuthme(),
                    new NoRot(),
                    new Insults(),
                    new ChatBypass(),
                    new Breaker(),
                    new AutoGroomer(),
                    new Nuker(),
                    new Timer(),
                    new Spammer(),
                    new Crasher(),
                    new PingSpoof(),
                    new NoGuiClose(),
                    new AntiSuffocation(),
                    new dev.risas.module.impl.other.PlayerHacks(),
                    new AntiCheat(),
                    //new StaffAlert(),
                    new Sniper(),
                    new ClientSpoofer(),
                    new AutoBuild(),
                    new Denicker(),
                    new ViaMCPFix(),
                    new Test(),

                    // RENDER
                    new ShaderESP(),
                    new PopOutAnimation(),
                    new ChinaHat(),
                    new Hitmarks(),
                    new SimsESP(),
                    new ClickGui(),
                    new Nametags(),
                    new Chams(),
                    new BrightPlayers(),
                    new ESP(),
                    new Animations(),
                    new Ambiance(),
                    new AttackEffects(),
                    new TargetHud(),
                    new DamageColor(),
                    new TwoDESP(),
                    new Enchant(),
                    new DeathEffects(),
                    new Zoot(),
                    new Fullbright(),
                    new NoWeather(),
                    new Giants(),
                    new Freecam(),
                    new Tracers(),
                    new NameProtect(),
                    new Streamer(),
                    new Scoreboard(),
                    new Breadcrumbs(),
                    new Radar(),
                    new Blur(),
                    new ChestESP(),
                    new NoHurtCam(),
                    new CameraClip(),
                    new ItemPhysics(),
                    new MotionGraph(),
                    new NoAchievements(),
                    new ImageESP(),
                    new Health(),
                    new XRay(),
                    new Particles(),

                    //SPECIAL ORDER (These modules events must be called in a specific order)
                    new AutoPot(),
                    new SSMode()
            };

            instance.notificationManager = new NotificationManager();

            instance.cmdManager = new CommandManager();

            CommandManager.COMMANDS = new Command[]{
                    new Bind(),
                    new Toggle(),
                    new Config(),
                    new Say(),
                    new Friend(),
                    new Crash(),
                    new Target(),
                    new ClientName(),
                    new VClip(),
                    new HClip(),
                    new Panic(),
                    new dev.risas.command.impl.Sniper(),
                    new dev.risas.command.impl.Spammer(),
                    new SetArea(),
                    new PlayerHacks(),
                    new Tp(),
                    new Name(),
                    new Help(),
                    new Denick(),
                    new Findnick(),
                    new Respawn()
            };
        } catch (final Exception e) {
            e.printStackTrace();
        }

        try {
            instance.clickGUI = new ClickGUI();
            instance.guiMainMenu = new MainMenu();
            instance.guiMultiplayer = new GuiMultiplayer(instance.guiMainMenu);
            instance.strikeGUI = new StrikeGUI();
            instance.altGUI = new GuiAltLogin();
            instance.altManagerGUI = new GuiAltLogin();
            instance.guiTheme = new GuiTheme();
            instance.antiCheat = new dev.risas.anticheat.AntiCheat();
            instance.scriptManager = new ScriptManager();
            instance.creativeTab = new RiseTab();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        final Minecraft mc = Minecraft.getMinecraft();

        // Compatibility
        mc.gameSettings.guiScale = 2;
        mc.gameSettings.ofFastRender = false;

        // Performance settings
        mc.gameSettings.ofSmartAnimations = true;
        mc.gameSettings.ofSmoothFps = false;

        try {
            // Creating Rise folder
            if (!FileUtil.riseDirectoryExists()) {
                instance.firstBoot = true;
                FileUtil.createRiseDirectory();
            }

            if (!FileUtil.exists("Config" + File.separator)) {
                FileUtil.createDirectory("Config" + File.separator);
            }

            if (!FileUtil.exists("Script" + File.separator)) {
                FileUtil.createDirectory("Script" + File.separator);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }

        try {
            instance.loadClient();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        AuthGUI.authed = true;
        mc.displayGuiScreen(instance.getGuiMainMenu());
    }


}
