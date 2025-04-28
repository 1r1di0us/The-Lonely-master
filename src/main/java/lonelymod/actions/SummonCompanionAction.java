package lonelymod.actions;

//import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;
import lonelymod.interfaces.RelicOnSummonInterface;

public class SummonCompanionAction extends AbstractGameAction {
    //one size fits all summon I have yet to make work
    private AbstractCompanion companion;
    private boolean onBattleStart;

    public SummonCompanionAction(AbstractCompanion companion) {
        this(companion, true);
    }

    public SummonCompanionAction(AbstractCompanion companion, boolean onBattleStart) {
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_FAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_LONG;
        }
        this.duration = this.startDuration;
        this.companion = companion;
        this.onBattleStart = onBattleStart;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
                CompanionField.currCompanion.set(AbstractDungeon.player, null);
            }
            CompanionField.currCompanion.set(AbstractDungeon.player, this.companion);
            this.companion.init();
            //this.c.animX = 1200.0F * Settings.xScale;
            this.companion.applyPowers();
        }
        tickDuration();
        if (this.isDone) {
            //this.c.animX = 0.0F;
            this.companion.showHealthBar();
            this.companion.useOnSummonAction(onBattleStart);
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if (r instanceof RelicOnSummonInterface) {
                    ((RelicOnSummonInterface) r).onSummon(this.companion, onBattleStart);
                }
            }
        }
        // else {
        //this.c.animX = Interpolation.fade.apply(0.0F, 1200.0F * Settings.xScale, this.duration);
        //}
    }
}
