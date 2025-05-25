package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.IntentFlashAction;
import com.megacrit.cardcrawl.actions.common.ShowMoveNameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;

public class CompanionTakeTurnAction extends AbstractGameAction {

    AbstractCompanion currCompanion;
    private final boolean callDefault;
    private final byte immediateCall;
    private final boolean triggerPowers;
    private final boolean silentCall;

    public CompanionTakeTurnAction(boolean callDefault, byte immediateCall, boolean immTriggerPowers, boolean immSilentCall) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            currCompanion = CompanionField.currCompanion.get(AbstractDungeon.player);
        }
        this.callDefault = callDefault;
        this.immediateCall = immediateCall;
        this.triggerPowers = immTriggerPowers;
        this.silentCall = immSilentCall;
    }
    public CompanionTakeTurnAction(boolean callDefault, byte immediateCall, boolean immTriggerPowers) {
        this(callDefault, immediateCall, immTriggerPowers, false); // 6 means none, so nothing happens with the others
    }
    public CompanionTakeTurnAction(boolean callDefault, byte immediateCall) {
        this(callDefault, immediateCall, true, false); // 6 means none, so nothing happens with the others
    }
    public CompanionTakeTurnAction(boolean callDefault) {
        this(callDefault, (byte) 6, false, false); // 6 means none, so nothing happens with the others
    }

    @Override
    public void update() {
        if (currCompanion != null && currCompanion == CompanionField.currCompanion.get(AbstractDungeon.player)) {
            if (currCompanion.intent != AbstractMonster.Intent.NONE) {
                if (callDefault) addToTop(new IntentFlashAction(currCompanion));
                else addToTop(new ImmediateIntentFlashAction(currCompanion)); // so that all the moves happening during your turn will go super fast
                addToTop(new ShowMoveNameAction(currCompanion, currCompanion.moveName));
            }
            // FTUE tip for monsters but could be useful for companions too if necessary.
            /*if (!TipTracker.tips.get("INTENT_TIP") && AbstractDungeon.player.currentBlock == 0 && (currCompanion.intent == AbstractMonster.Intent.ATTACK || currCompanion.intent == AbstractMonster.Intent.ATTACK_DEBUFF || currCompanion.intent == AbstractMonster.Intent.ATTACK_BUFF || currCompanion.intent == AbstractMonster.Intent.ATTACK_DEFEND))
                if (AbstractDungeon.floorNum <= 5) {
                    TipTracker.blockCounter++;
                } else {
                    TipTracker.neverShowAgain("INTENT_TIP");
                }*/
            currCompanion.takeTurn(callDefault);
            currCompanion.applyTurnPowers();
            if (immediateCall != (byte) 6) { // cover all bases i guess, maybe some day we'll do wacky things
                addToBot(new ImmediateCallMoveAction(immediateCall, currCompanion, triggerPowers, silentCall));
            }
        }
        this.isDone = true;
    }
}
