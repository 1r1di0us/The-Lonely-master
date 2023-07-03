package lonelymod.actions;

import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.LonelyCharacter;
import lonelymod.companions.AbstractCompanion;
import lonelymod.companions.Bones;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SummonBonesAction extends AbstractGameAction {


    private static final Logger logger = LogManager.getLogger(SummonBonesAction.class.getName());
    private AbstractCompanion c;

    public SummonBonesAction() {
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_FAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_LONG;
        }
        this.duration = this.startDuration;
        int slot = identifySlot();
        if (slot == -1) {
            logger.info("INCORRECTLY ATTEMPTED TO SUMMON COMPANION.");
            return;
        }
        this.c = new Bones(-750, -25);
        LonelyCharacter.currCompanion = this.c;
    }

    private int identifySlot() {
        if (LonelyCharacter.currCompanion == null)
            return 1;
        return -1;
    }

    private int getSmartPosition() {
    int position = 0;
    for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
      if (this.c.drawX > mo.drawX)
        position++;
    }
    return position;
  }

    public void update() {
        if (this.duration == this.startDuration) {
            this.c.animX = 1200.0F * Settings.xScale;
            this.c.init();
            this.c.applyPowers();
        }
        tickDuration();
        if (this.isDone) {
            this.c.animX = 0.0F;
            this.c.showHealthBar();
            this.c.usePreBattleAction();
        } else {
            this.c.animX = Interpolation.fade.apply(0.0F, 1200.0F * Settings.xScale, this.duration);

        }
    }
}
