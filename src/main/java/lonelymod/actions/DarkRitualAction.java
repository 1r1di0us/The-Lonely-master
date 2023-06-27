package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.RitualPower;

public class DarkRitualAction extends AbstractGameAction {
    private float startingDuration;
    
    public DarkRitualAction() {
        this.target = AbstractDungeon.player;
        this.actionType = AbstractGameAction.ActionType.WAIT;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    public void update() { 
        if (this.duration == this.startingDuration) {
            int count = AbstractDungeon.player.hand.size();
            if (count != 0) {
                addToTop((AbstractGameAction)new ApplyPowerAction(target, target, new RitualPower(target, count, true), count));
                addToTop((AbstractGameAction)new DiscardAction(this.target, this.target, count, true));
            } 
            this.isDone = true;
        }
    }
}
