package lonelymod.actions;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class OverpowerAction extends AbstractGameAction {
    private AbstractMonster m;

    public OverpowerAction(int weakAmt, AbstractMonster m) {
        this.actionType = AbstractGameAction.ActionType.WAIT;
        this.amount = weakAmt;
        this.m = m;
    }

    public void update() {
        if (this.m != null && !(this.m.getIntentBaseDmg() >= 0))
            addToTop(new ApplyPowerAction(this.m, AbstractDungeon.player, new WeakPower(this.m, this.amount, false), this.amount));
        this.isDone = true;
    }
}
