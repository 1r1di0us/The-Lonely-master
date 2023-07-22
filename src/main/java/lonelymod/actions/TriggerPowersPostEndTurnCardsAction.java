package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lonelymod.interfaces.AtEndOfTurnPostEndTurnCardsInterface;

public class TriggerPowersPostEndTurnCardsAction extends AbstractGameAction {

    public TriggerPowersPostEndTurnCardsAction(AbstractCreature target) {
        this.target = target;
    }
    public void update() {
        for (AbstractPower p : target.powers) {
            if (p instanceof AtEndOfTurnPostEndTurnCardsInterface) {
                ((AtEndOfTurnPostEndTurnCardsInterface) p).atEndOfTurnPostEndTurnCards(target.isPlayer);
            }
        }
        this.isDone = true;
    }
}
