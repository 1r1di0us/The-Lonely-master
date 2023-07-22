package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import lonelymod.interfaces.AtEndOfTurnPostEndTurnCardsInterface;

public class TriggerPostEndTurnCardsAction extends AbstractGameAction {

    public TriggerPostEndTurnCardsAction(AbstractCreature target) {
        this.target = target;
    }
    public void update() {
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof AtEndOfTurnPostEndTurnCardsInterface) {
                ((AtEndOfTurnPostEndTurnCardsInterface) r).atEndOfTurnPostEndTurnCards(true);
            }
        }
        for (AbstractPower p : target.powers) {
            if (p instanceof AtEndOfTurnPostEndTurnCardsInterface) {
                ((AtEndOfTurnPostEndTurnCardsInterface) p).atEndOfTurnPostEndTurnCards(target.isPlayer);
            }
        }
        this.isDone = true;
    }
}
