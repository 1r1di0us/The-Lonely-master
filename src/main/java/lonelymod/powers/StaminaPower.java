package lonelymod.powers;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import basemod.interfaces.CloneablePowerInterface;
import lonelymod.LonelyMod;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;
import lonelymod.interfaces.ModifyCompanionBlockInterface;
import lonelymod.util.TexLoader;

public class StaminaPower extends AbstractEasyPower implements CloneablePowerInterface, ModifyCompanionBlockInterface {

    public static final String POWER_ID = makeID("StaminaPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Stamina84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Stamina32.png");

    public StaminaPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, false, owner, amount);

        this.owner = owner;

        type = PowerType.BUFF;
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

    public void onSpecificTrigger() { //when companion blocks
        flash();
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    @Override
    public float modifyBlock(float blockAmount) {
        blockAmount += this.amount;
        return blockAmount;
    }

    @Override
    public float modifyCompanionBlock(float blockAmount, AbstractCompanion companion) {
        if (this.owner instanceof AbstractCompanion)
            blockAmount += this.amount;
        return blockAmount;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.baseBlock >= 0) { //don't forget suppress and co.
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void updateDescription() {
        if (owner == AbstractDungeon.player) description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[3];
        else if (owner == CompanionField.currCompanion.get(AbstractDungeon.player)) description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[3];
        else description = DESCRIPTIONS[2] + amount + DESCRIPTIONS[3];
    }

    @Override
    public AbstractPower makeCopy() {
        return new StaminaPower(this.owner, this.amount);
    }
}
