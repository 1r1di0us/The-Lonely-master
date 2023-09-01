package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AgainstAllOddsAction extends AbstractGameAction {

    private int damageCap;

    public AgainstAllOddsAction(int energyAmt, int damageCap) {
        this.actionType = AbstractGameAction.ActionType.WAIT;
        this.amount = energyAmt;
        this.damageCap = damageCap;
    }

    public void update() {
        if (AbstractDungeon.player.currentHealth <= damageCap)
            addToTop(new GainEnergyAction(this.amount));
        this.isDone = true;
    }
}
