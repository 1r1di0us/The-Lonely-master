package lonelymod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lonelymod.LonelyMod;
import lonelymod.fields.CompanionField;
import lonelymod.util.TexLoader;

import static lonelymod.LonelyMod.makeID;

public class TargetPower extends AbstractEasyPower implements CloneablePowerInterface {

    public static final String POWER_ID = makeID("TargetPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Target84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Target32.png");

    private boolean justApplied = false;
    public TargetPower(AbstractCreature owner, int amount, boolean isSourceCompanion) {
        super(POWER_ID, NAME, AbstractPower.PowerType.DEBUFF, true, owner, amount);

        this.owner = owner;

        type = PowerType.DEBUFF;
        isTurnBased = true;
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
        if (AbstractDungeon.actionManager.turnHasEnded && isSourceCompanion)
            this.justApplied = true;
    }

    @Override
    public void onInitialApplication() {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            CompanionField.currCompanion.get(AbstractDungeon.player).getTarget();
            CompanionField.currCompanion.get(AbstractDungeon.player).applyPowers();
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null
                && CompanionField.currCompanion.get(AbstractDungeon.player).targetAmount > this.amount) {
            CompanionField.currCompanion.get(AbstractDungeon.player).getTarget();
            CompanionField.currCompanion.get(AbstractDungeon.player).applyPowers();
        }
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null
                && CompanionField.currCompanion.get(AbstractDungeon.player).targetAmount == this.amount+reduceAmount) {
            CompanionField.currCompanion.get(AbstractDungeon.player).getTarget();
            CompanionField.currCompanion.get(AbstractDungeon.player).applyPowers();
        }
    }

    @Override
    public void atEndOfRound() {
        if (justApplied) {
            justApplied = false;
            return;
        }
        if (this.amount == 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
        else {
            addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }
    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }
    @Override
    public AbstractPower makeCopy() {
        return new TargetPower(this.owner, this.amount, this.justApplied);
    }
}
