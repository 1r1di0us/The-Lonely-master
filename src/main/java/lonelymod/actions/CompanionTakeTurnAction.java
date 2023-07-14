package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.IntentFlashAction;
import com.megacrit.cardcrawl.actions.common.ShowMoveNameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;

public class CompanionTakeTurnAction extends AbstractGameAction {

    AbstractCompanion currCompanion;

    public CompanionTakeTurnAction() {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            currCompanion = CompanionField.currCompanion.get(AbstractDungeon.player);
        }
    }
    @Override
    public void update() {
        if (currCompanion != null && currCompanion == CompanionField.currCompanion.get(AbstractDungeon.player)) {
            if (currCompanion.intent != AbstractMonster.Intent.NONE) {
                addToBot(new ShowMoveNameAction(currCompanion));
                addToBot(new IntentFlashAction(currCompanion));
            }
            if (!(TipTracker.tips.get("INTENT_TIP")).booleanValue() && AbstractDungeon.player.currentBlock == 0 && (currCompanion.intent == AbstractMonster.Intent.ATTACK || currCompanion.intent == AbstractMonster.Intent.ATTACK_DEBUFF || currCompanion.intent == AbstractMonster.Intent.ATTACK_BUFF || currCompanion.intent == AbstractMonster.Intent.ATTACK_DEFEND))
                if (AbstractDungeon.floorNum <= 5) {
                    TipTracker.blockCounter++;
                } else {
                    TipTracker.neverShowAgain("INTENT_TIP");
                }
            currCompanion.takeTurn();
            currCompanion.applyTurnPowers();
        }
        this.isDone = true;
    }
}
