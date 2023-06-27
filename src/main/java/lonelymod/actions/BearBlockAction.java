package lonelymod.actions;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BearBlockAction extends AbstractGameAction {
    private int blockAmount;
    private int numTimes;

    public BearBlockAction(AbstractCreature target, int blockAmount, int numTimes) {
        this.blockAmount = blockAmount;
        this.numTimes = numTimes;

        this.target = target;
        this.actionType = ActionType.BLOCK;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        for (int i = 0; i < numTimes; i++) {
            addToTop(new GainBlockAction(target, target, blockAmount));
        }
        if (AbstractDungeon.player.hasPower(makeID("StaminaPower"))) {
            addToTop(new RemoveSpecificPowerAction(target, target, AbstractDungeon.player.getPower(makeID("StaminaPower"))));
        }
        this.isDone = true;
    }
    
}
