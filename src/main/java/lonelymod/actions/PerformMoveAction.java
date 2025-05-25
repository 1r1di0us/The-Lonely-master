package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import lonelymod.companions.AbstractCompanion;

public class PerformMoveAction extends AbstractGameAction {

    private final byte move;
    private final AbstractCompanion currCompanion;
    //Done by frenzy, special sauce, and multi-wild form. Use CompanionTakeTurnAction if you want the move name to show up, and to use addToBot's like a good boy.
    public PerformMoveAction(byte move, AbstractCompanion currCompanion) {
        this.move = move;
        this.currCompanion = currCompanion;
    }

    public void update() {
        currCompanion.nextMove = move;
        currCompanion.flashIntent();
        currCompanion.performImmediately(move);
        currCompanion.applyTurnPowers();
        this.isDone = true;
    }
}
