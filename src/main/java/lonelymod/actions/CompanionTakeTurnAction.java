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
    private final boolean callDefault;

    public CompanionTakeTurnAction(boolean callDefault) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            currCompanion = CompanionField.currCompanion.get(AbstractDungeon.player);
        }
        this.callDefault = callDefault;
    }
    @Override
    public void update() {
        if (currCompanion != null && currCompanion == CompanionField.currCompanion.get(AbstractDungeon.player)) {
            if (currCompanion.intent != AbstractMonster.Intent.NONE) {
                addToTop(new IntentFlashAction(currCompanion));
                addToTop(new ShowMoveNameAction(currCompanion, currCompanion.moveName));
            }
            if (!TipTracker.tips.get("INTENT_TIP") && AbstractDungeon.player.currentBlock == 0 && (currCompanion.intent == AbstractMonster.Intent.ATTACK || currCompanion.intent == AbstractMonster.Intent.ATTACK_DEBUFF || currCompanion.intent == AbstractMonster.Intent.ATTACK_BUFF || currCompanion.intent == AbstractMonster.Intent.ATTACK_DEFEND))
                if (AbstractDungeon.floorNum <= 5) {
                    TipTracker.blockCounter++;
                } else {
                    TipTracker.neverShowAgain("INTENT_TIP");
                }
            currCompanion.performTurn(callDefault);
            currCompanion.applyTurnPowers();
        }
        this.isDone = true;
    }
}
