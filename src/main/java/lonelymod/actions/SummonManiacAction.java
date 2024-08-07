package lonelymod.actions;

//import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import lonelymod.companions.AbstractCompanion;
import lonelymod.companions.Bones;
import lonelymod.companions.Maniac;
import lonelymod.fields.CompanionField;
import lonelymod.interfaces.RelicOnSummonInterface;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.lastCombatMetricKey;

public class SummonManiacAction extends AbstractGameAction {
    private AbstractCompanion c;

    public SummonManiacAction() {
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_FAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_LONG;
        }
        this.duration = this.startDuration;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
                if (CompanionField.currCompanion.get(AbstractDungeon.player) instanceof Maniac) {
                    this.isDone = true;
                    return;
                }
                CompanionField.currCompanion.set(AbstractDungeon.player, null);
            }
            this.c = new Maniac();
            CompanionField.currCompanion.set(AbstractDungeon.player, this.c);
            this.c.init();
            //this.c.animX = 1200.0F * Settings.xScale;
            this.c.applyPowers();
        }
        tickDuration();
        if (this.isDone) {
            //this.c.animX = 0.0F;
            this.c.showHealthBar();
            this.c.usePreBattleAction();
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if (r instanceof RelicOnSummonInterface) {
                    ((RelicOnSummonInterface) r).onSummon(this.c, true);
                }
            }
        }
        // else {
        //this.c.animX = Interpolation.fade.apply(0.0F, 1200.0F * Settings.xScale, this.duration);
        //}
    }
}
