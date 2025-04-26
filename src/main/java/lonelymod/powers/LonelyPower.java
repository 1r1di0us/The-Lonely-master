package lonelymod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lonelymod.LonelyMod;
import lonelymod.util.TexLoader;

import static lonelymod.LonelyMod.makeID;

public class LonelyPower extends AbstractEasyPower implements CloneablePowerInterface, NonStackablePower {
    public static final String POWER_ID = makeID("LonelyPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Lonely84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Lonely32.png");

    public int maxAmount;

    public LonelyPower(AbstractCreature owner, int maxAmt) {
        super(POWER_ID, NAME, AbstractPower.PowerType.DEBUFF, false, owner, -1);

        this.owner = owner;

        type = PowerType.DEBUFF;
        isTurnBased = false;
        this.maxAmount = maxAmt;

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
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        //actual not playing of the card happens in TargetCompanionWithCardPatch
        this.amount++;
        if (this.amount < this.maxAmount - 2) {
            flashWithoutSound();
        } else {
            flash();
            if (this.amount == this.maxAmount) this.amount = 0;
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.maxAmount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new LonelyPower(this.owner, this.maxAmount);
    }
}
