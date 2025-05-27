package lonelymod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lonelymod.LonelyMod;
import lonelymod.actions.OraclePowerAction;
import lonelymod.util.TexLoader;

import static lonelymod.LonelyMod.makeID;

public class OraclePower extends AbstractEasyPower implements CloneablePowerInterface {

    public static final String POWER_ID = makeID("OraclePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Oracle84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Oracle32.png");

    public OraclePower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, true, owner, amount);

        this.owner = owner;
        this.amount = amount;

        type = AbstractPower.PowerType.BUFF;
        isTurnBased = true;

        if (tex84 != null) {
            region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, tex84.getWidth(), tex84.getHeight());
            if (tex32 != null)
                region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, tex32.getWidth(), tex32.getHeight());
        } else if (tex32 != null) {
            this.img = tex32;
            region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, tex32.getWidth(), tex32.getHeight());
        }

        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + (this.amount*2) + DESCRIPTIONS[2] + (this.amount*3) + DESCRIPTIONS[3];
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new OraclePowerAction(this.owner, this.amount, this.amount*2, this.amount*3)); // make it an action so it happens after Simmering Fury
    }

    @Override
    public AbstractPower makeCopy() {
        return new OraclePower(this.owner, this.amount);
    }
}
