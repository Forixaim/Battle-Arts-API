package net.forixaim.battle_arts_api.battle_arts_skills;

import com.yesman.epicskills.client.gui.screen.CategorySlotTexture;

public enum BattleArtsTextures implements CategorySlotTexture
{
    BATTLE_STYLE(6, 6, 44, 44),
    MANA_ART(6, 6, 44, 44);
    private final int offsetX;
    private final int offsetY;
    private final int texWidth;
    private final int texHeight;
    private final int universalOrder;

    BattleArtsTextures(int offsetX, int offsetY, int texWidth, int texHeight) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.universalOrder = CategorySlotTexture.ENUM_MANAGER.assign(this);
    }

    public int offsetX() {
        return this.offsetX;
    }

    public int offsetY() {
        return this.offsetY;
    }

    public int texWidth() {
        return this.texWidth;
    }

    public int texHeight() {
        return this.texHeight;
    }

    public int universalOrdinal() {
        return this.universalOrder;
    }
}
