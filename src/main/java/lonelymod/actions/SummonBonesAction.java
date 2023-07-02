package lonelymod.actions;

import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lonelymod.companions.Bones3;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SummonBonesAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(SummonBonesAction.class.getName());
    private AbstractMonster m;

    public SummonBonesAction(AbstractMonster[] companions) {
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_FAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_LONG;
        }
        this.duration = this.startDuration;
        int slot = identifySlot(companions);
        if (slot == -1) {
            logger.info("INCORRECTLY ATTEMPTED TO SUMMON COMPANION.");
            return;
        }
        this.m = new Bones3(-750, -25);
        companions[slot] = this.m;
        for (AbstractRelic r : AbstractDungeon.player.relics)
            r.onSpawnMonster(this.m);
    }

    private int identifySlot(AbstractMonster[] companions) {
        for (int i = 0; i < companions.length; i++) {
            if (companions[i] == null || (companions[i]).isDying)
                return i;
        }
        return -1;
    }

    private int getSmartPosition() {
        int position = 0;
        for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (this.m.drawX > mo.drawX)
                position++;
        }
        return position;
    }
    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            //this.m.animX = 1200.0F * Settings.xScale;
            this.m.init();
            this.m.applyPowers();
            //(AbstractDungeon.getCurrRoom()).monsters.addMonster(getSmartPosition(), this.m);
        }
        tickDuration();
        if (this.isDone) {
            this.m.animX = 0.0F;
            this.m.hideHealthBar();
            //this.m.usePreBattleAction();
        } else {
            this.m.animX = Interpolation.fade.apply(0.0F, 1200.0F * Settings.xScale, this.duration);
        }
    }
}
