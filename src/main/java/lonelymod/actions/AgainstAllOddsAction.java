package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AgainstAllOddsAction extends AbstractGameAction {

    public AgainstAllOddsAction(int energyAmt) {
        this.actionType = AbstractGameAction.ActionType.WAIT;
        this.amount = energyAmt;
    }

    public void update() {
        boolean GainEnergy = true;
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters)
            if (!(m != null && m.getIntentBaseDmg() >= 0))
                GainEnergy = false;
        if (GainEnergy)
            addToTop(new GainEnergyAction(this.amount));
        this.isDone = true;
    }
}
