package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import lonelymod.companions.AbstractCompanion;

public class WildFormCallMoveAction extends AbstractGameAction {

    private final byte move;
    private final AbstractCompanion currCompanion;

    public WildFormCallMoveAction(byte move, AbstractCompanion currCompanion) {
        this.move = move;
        this.currCompanion = currCompanion;
    }

    public void update() {
        switch (move) {
            case 1:
                currCompanion.callMainMove(AbstractCompanion.ATTACK, false);
                break;
            case 2:
                currCompanion.callMainMove(AbstractCompanion.PROTECT, false);
                break;
            case 3:
                currCompanion.callMainMove(AbstractCompanion.SPECIAL, false);
                break;
        }
        currCompanion.performTurn(move);
        currCompanion.applyTurnPowers();
        currCompanion.callDefault();
        this.isDone = true;
    }
}
