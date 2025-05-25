package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ImmediateIntentFlashAction extends AbstractGameAction {
    private AbstractMonster m;

    public ImmediateIntentFlashAction(AbstractMonster m) {
        if (Settings.FAST_MODE) {
            this.startDuration = 0.1F; //we want fast fast
        } else {
            this.startDuration = Settings.ACTION_DUR_FASTER; // this is 0.2F
        }
        this.duration = this.startDuration;
        this.m = m;
        this.actionType = AbstractGameAction.ActionType.WAIT;
    }

    public void update() {
        if (this.duration == this.startDuration)
            this.m.flashIntent();
        tickDuration();
    }
}