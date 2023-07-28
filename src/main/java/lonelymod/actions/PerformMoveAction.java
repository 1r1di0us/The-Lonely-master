package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import lonelymod.companions.AbstractCompanion;

public class PerformMoveAction extends AbstractGameAction {

    private final byte move;
    private final AbstractCompanion currCompanion;

    public PerformMoveAction(byte move, AbstractCompanion currCompanion) {
        this.move = move;
        this.currCompanion = currCompanion;
    }

    public void update() {
        currCompanion.nextMove = move;
        currCompanion.flashIntent();
        currCompanion.performMove(move);
        currCompanion.applyTurnPowers();
        this.isDone = true;
    }
}
