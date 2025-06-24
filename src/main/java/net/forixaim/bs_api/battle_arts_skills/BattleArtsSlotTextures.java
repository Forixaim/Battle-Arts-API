package net.forixaim.bs_api.battle_arts_skills;

import com.yesman.epicskills.client.gui.screen.CategorySlotTexture;

public enum BattleArtsSlotTextures implements CategorySlotTexture
{
    BATTLE_STYLE(6, 6, 44, 44),
    MANA_ART(6, 6, 44, 44);

    private final int offsetX;
    private final int offsetY;
    private final int texWidth;
    private final int texHeight;
    private final int universalOrder;

    BattleArtsSlotTextures(int offsetX, int offsetY, int texWidth, int texHeight) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.universalOrder = CategorySlotTexture.ENUM_MANAGER.assign(this);
    }

    @Override
    public int offsetX() {
        return this.offsetX;
    }

    @Override
    public int offsetY() {
        return this.offsetY;
    }

    @Override
    public int texWidth() {
        return this.texWidth;
    }

    @Override
    public int texHeight() {
        return this.texHeight;
    }

    @Override
    public int universalOrdinal() {
        return this.universalOrder;
    }
}
