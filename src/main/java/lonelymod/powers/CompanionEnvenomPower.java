package lonelymod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;;

import static lonelymod.LonelyMod.makeID;

public class CompanionEnvenomPower extends AbstractEasyPower {
    public static final String POWER_ID = makeID("CompanionEnvenomPower");

    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public CompanionEnvenomPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, false, owner, amount);
        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;
        this.amount = amount;

        loadRegion("envenom");

        updateDescription();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (damageAmount > 0 && target != AbstractDungeon.player && info.owner == this.owner) {
            flash();
            addToTop(new ApplyPowerAction(target, this.owner, new PoisonPower(target, AbstractDungeon.player, this.amount), this.amount, true));
            // the player applied the poison so this doesn't trigger when the poison deals damage
        }
    }
}