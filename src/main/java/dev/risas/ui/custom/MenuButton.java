package dev.risas.ui.custom;

import dev.risas.font.CustomFont;
import dev.risas.font.fontrenderer.TTFFontRenderer;
import dev.risas.util.render.RenderUtil;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class MenuButton {

    public final String text;
    public float x, y, width, height;
    public Runnable clickAction;

    public MenuButton(String text) {
        this.text = text;
    }

    public void drawScreen(int mouseX, int mouseY) {
        final TTFFontRenderer fontRenderer = CustomFont.FONT_MANAGER.getFont("Dreamscape 96");
        boolean hovering = RenderUtil.isHovering(x, y, width, height, mouseX, mouseY);

        Gui.drawRect((int) x, (int) y, (int) (x + width), (int) (y + height), hovering ? new Color(0, 0, 0, 90).getRGB() : new Color(0, 0, 0, 120).getRGB());
        fontRenderer.drawCenteredString(text, (int) (x + width / 2f), (int) (y + (height / 2f - (fontRenderer.getHeight(text) / 2f))), -1);
    }

    public void mouseClicked(int mouseX, int mouseY) {
        if (RenderUtil.isHovering(x, y, width, height, mouseX, mouseY)) clickAction.run();
    }

    public String getText() {
        return text;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}