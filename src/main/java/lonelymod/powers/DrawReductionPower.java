package lonelymod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DrawReductionPower extends AbstractEasyPower implements CloneablePowerInterface {
    public static final String POWER_ID = "Draw Reduction";

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Draw Reduction");

    public static final String NAME = powerStrings.NAME;

    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean justApplied;

    public DrawReductionPower(AbstractCreature owner, int amount, boolean isSourceMonster) {
        super(POWER_ID, NAME, AbstractPower.PowerType.DEBUFF, true, owner, amount);
        this.name = NAME;
        this.ID = "Draw Reduction";
        this.owner = owner;
        this.amount = amount;
        this.justApplied = isSourceMonster;
        updateDescription();
        loadRegion("lessdraw");
        this.type = AbstractPower.PowerType.DEBUFF;
        this.isTurnBased = true;
    }

    public void onInitialApplication() {
        AbstractDungeon.player.gameHandSize--;
    }

    public void atEndOfRound() {
        if (this.justApplied) {
            this.justApplied = false;
            return;
        }
        addToBot(new ReducePowerAction(this.owner, this.owner, "Draw Reduction", 1));
    }

    public void onRemove() {
        AbstractDungeon.player.gameHandSize++;
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }

    public AbstractPower makeCopy() {
        return new DrawReductionPower(this.owner, this.amount, this.justApplied);
    }
}
