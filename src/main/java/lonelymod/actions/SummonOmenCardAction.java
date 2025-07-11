package lonelymod.actions;

//import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lonelymod.companions.AbstractCompanion;
import lonelymod.companions.Omen;
import lonelymod.fields.CompanionField;
import lonelymod.interfaces.RelicOnSummonInterface;

public class SummonOmenCardAction extends AbstractGameAction {
    private AbstractCompanion c;
    private final boolean onBattleStart;

    public SummonOmenCardAction(boolean onBattleStart) {
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_FAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_LONG;
        }
        this.duration = this.startDuration;
        this.onBattleStart = onBattleStart;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
                if (CompanionField.currCompanion.get(AbstractDungeon.player) instanceof Omen) {
                    this.isDone = true;
                    return;
                }
                CompanionField.currCompanion.set(AbstractDungeon.player, null);
            }
            this.c = new Omen();
            CompanionField.currCompanion.set(AbstractDungeon.player, this.c);
            //in case you have wild form, and you use omen then default move doesn't happen
            this.c.callMove(AbstractCompanion.NONE); // CallMoveAction is too slow here.
            this.c.healthBarUpdatedEvent();
            //this.c.createIntent(); // moved to callMove()
            CompanionField.playableCards.set(AbstractDungeon.player, null);
            //this.c.animX = 1200.0F * Settings.xScale;
            this.c.applyPowers();
        }
        tickDuration();
        if (this.isDone) {
            //this.c.animX = 0.0F;
            this.c.showHealthBar();
            this.c.useOnSummonAction(onBattleStart);
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if (r instanceof RelicOnSummonInterface) {
                    ((RelicOnSummonInterface) r).onSummon(this.c, false);
                }
            }
            addToBot(new CallMoveAction(AbstractCompanion.SPECIAL, CompanionField.currCompanion.get(AbstractDungeon.player)));
        }// else {
        //this.c.animX = Interpolation.fade.apply(0.0F, 1200.0F * Settings.xScale, this.duration);
        //}
    }
}
