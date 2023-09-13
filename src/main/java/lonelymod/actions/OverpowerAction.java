package lonelymod.actions;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class OverpowerAction extends AbstractGameAction {
    private final AbstractMonster m;

    public OverpowerAction(int weakAmt, AbstractMonster m) {
        this.actionType = AbstractGameAction.ActionType.WAIT;
        this.amount = weakAmt;
        this.m = m;
    }

    public void update() {
        if (this.m != null) {
            int multiAmt = ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentMultiAmt");
            if ((multiAmt <= 1 && AbstractDungeon.player.currentBlock >= m.getIntentDmg())
                    || (multiAmt > 1 && AbstractDungeon.player.currentBlock >= m.getIntentDmg() * multiAmt)) {
                addToTop(new ApplyPowerAction(this.m, AbstractDungeon.player, new WeakPower(this.m, this.amount, false), this.amount));
            }
        }
        this.isDone = true;
    }
}
