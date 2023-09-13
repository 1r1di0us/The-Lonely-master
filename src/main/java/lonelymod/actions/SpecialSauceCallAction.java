package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import lonelymod.companions.AbstractCompanion;

public class SpecialSauceCallAction extends AbstractGameAction {

    private final AbstractCompanion currCompanion;
    private final byte prevMove;

    public SpecialSauceCallAction(AbstractCompanion currCompanion) {
        this.currCompanion = currCompanion;
        this.prevMove = currCompanion.nextMove;
    }

    public void update() {
        currCompanion.callMainMove(AbstractCompanion.SPECIAL, false, false, true);
        currCompanion.performMove(AbstractCompanion.SPECIAL);
        currCompanion.applyTurnPowers();
        switch (prevMove) {
            case AbstractCompanion.DEFAULT:
                currCompanion.callDefault();
                break;
            case AbstractCompanion.ATTACK:
                currCompanion.callMainMove(AbstractCompanion.ATTACK, false, false, false);
                break;
            case AbstractCompanion.PROTECT:
                currCompanion.callMainMove(AbstractCompanion.PROTECT, false, false, false);
                break;
            case AbstractCompanion.SPECIAL:
                currCompanion.callMainMove(AbstractCompanion.SPECIAL, false, false, false);
                break;
        }
        this.isDone = true;
    }
}
