package lonelymod.actions;

//import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.companions.AbstractCompanion;
import lonelymod.companions.Mechanic;
import lonelymod.fields.CompanionField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SummonMechanicAction extends AbstractGameAction {
    private static final Logger logger = LogManager.getLogger(SummonMechanicAction.class.getName());
    private AbstractCompanion c;
    private boolean summon = true;

    public SummonMechanicAction() {
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_FAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_LONG;
        }
        this.duration = this.startDuration;
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            logger.info("INCORRECTLY ATTEMPTED TO SUMMON COMPANION.");
            this.summon = false;
            return;
        }
        this.c = new Mechanic(750, -25);
        CompanionField.currCompanion.set(AbstractDungeon.player, this.c);
        this.c.init();
    }

    public void update() {
        if (summon) {
            if (this.duration == this.startDuration) {
                //this.c.animX = 1200.0F * Settings.xScale;
                this.c.applyPowers();
            }
            tickDuration();
            if (this.isDone) {
                //this.c.animX = 0.0F;
                this.c.showHealthBar();
                this.c.usePreBattleAction();
            }
            // else {
            //this.c.animX = Interpolation.fade.apply(0.0F, 1200.0F * Settings.xScale, this.duration);
            //}
        } else {
            this.isDone = true;
        }
    }
}