package dev.risas.ui.altmanager2.alt;

import dev.risas.ui.custom.MenuButton;
import dev.risas.ui.mainmenu.MainMenu;
import dev.risas.util.alt.AltUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;

import java.io.IOException;

public class GuiAltLogin extends GuiScreen {

    private GuiTextField username;
    private PasswordField password;
    private String status;

    private final MenuButton[] buttons = {
            new MenuButton("Login"),
            new MenuButton("Back"),
            new MenuButton("External Login"),
            new MenuButton("Generate Random Account")
    };

    @Override
    public void initGui() {
        username = new GuiTextField(height / 4 + 24, mc.fontRendererObj, width / 2 - 100, 60, 200, 20, true);
        password = new PasswordField(mc.fontRendererObj, width / 2 - 100, 100, 200, 20);

        int buttonHeight = 12;
        for(MenuButton button : buttons) {
            button.setX(width / 2f - 100);
            button.setY(height / 4f + 80 + buttonHeight);
            button.setWidth(200);
            button.setHeight(20);
            buttonHeight += 24;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        username.drawTextBox();
        password.drawTextBox();

        mc.fontRendererObj.drawCenteredStringWithShadow("Alt Login", (int) (width / 2.0F), 20, -1);
        mc.fontRendererObj.drawCenteredStringWithShadow(getStatus() != null ? getStatus() : EnumChatFormatting.YELLOW + "Idle...", (int) (width / 2.0F), 29, -1);

        if (username.getText().isEmpty()) mc.fontRendererObj.drawString("Username", (int) (width / 2.0F - 96), 66, -7829368);
        if (password.getText().isEmpty()) mc.fontRendererObj.drawString("Password", (int) (width / 2.0F - 96), 106, -7829368);

        for(MenuButton button : buttons) {
            button.clickAction = () -> {
                switch (button.getText()) {
                    case "Login":
                        if (!username.getText().isEmpty()) {
                            if (password.getText().isEmpty()) {
                                mc.session = (new Session(username.getText(), "", "", "mojang"));
                                setStatus(EnumChatFormatting.GREEN + "Logged into " + username.getText() + " (CRACKED ACCOUNT)");
                            } else
                                setStatus(EnumChatFormatting.RED + "This feature is currently on maintance.");
                        } else
                            setStatus(EnumChatFormatting.RED + "Username cannot be blank.");
                        break;
                    case "Back":
                        mc.displayGuiScreen(new MainMenu());
                        break;
                    case "External Login":
                        try {
                            setStatus(EnumChatFormatting.YELLOW + "Link has been copied to your clipboard.");
                            new ExternalLogin(this).getAccessToken();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case "Generate Random Account":
                        mc.session = new Session(AltUtil.generateName(), "", "", "mojang");
                        setStatus(EnumChatFormatting.GREEN + "Logged into " + mc.getSession().getUsername() + " (CRACKED ACCOUNT)");
                        break;
                }
            };

            button.drawScreen(mouseX, mouseY);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        try {
            super.keyTyped(typedChar, keyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        username.textboxKeyTyped(typedChar, keyCode);
        password.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            e.printStackTrace();
        }

        username.mouseClicked(mouseX, mouseY, mouseButton);
        password.mouseClicked(mouseX, mouseY, mouseButton);

        for (MenuButton button : buttons)
            button.mouseClicked(mouseX, mouseY);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}