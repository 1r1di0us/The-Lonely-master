package lonelymod.powers;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.powers.StrengthPower;
import lonelymod.LonelyMod;
import lonelymod.fields.CompanionField;
import lonelymod.util.TexLoader;

public class FetchPower extends AbstractEasyPower implements CloneablePowerInterface {
   
    public static final String POWER_ID = makeID("FetchPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Fetch84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Fetch32.png");

    private boolean isUsed = false;

    public FetchPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, AbstractPower.PowerType.DEBUFF, false, owner, amount);

        this.owner = owner;

        type = PowerType.DEBUFF;
        isTurnBased = false;
        this.amount = amount;

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
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.owner == CompanionField.currCompanion.get(AbstractDungeon.player) && !this.isUsed) {
            flash();
            addToBot(new ApplyPowerAction(CompanionField.currCompanion.get(AbstractDungeon.player), owner, new StrengthPower(CompanionField.currCompanion.get(AbstractDungeon.player), this.amount), this.amount));
            this.isUsed = true;
            addToBot(new RemoveSpecificPowerAction(this.owner, CompanionField.currCompanion.get(AbstractDungeon.player), this));
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new FetchPower(this.owner, this.amount);
    }
}
