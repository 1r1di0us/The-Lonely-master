package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lonelymod.fields.ReturnField;
import lonelymod.interfaces.TriggerOnReturnInterface;

public class ReturnAllCardsAction extends AbstractGameAction {

    private final boolean turnOffReturn;

    public ReturnAllCardsAction(boolean turnOffReturn) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.turnOffReturn = turnOffReturn;
    }

    public void update() {
        boolean returnedACard = false;
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (ReturnField.willReturn.get(c)) {
                if (turnOffReturn)
                    ReturnField.willReturn.set(c, false);
                addToBot(new ReturnToHandAction(c));
                if (c instanceof TriggerOnReturnInterface) {
                    ((TriggerOnReturnInterface) c).triggerOnReturn();
                }
                if (!returnedACard) returnedACard = true;
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (ReturnField.willReturn.get(c)) {
                if (turnOffReturn)
                    ReturnField.willReturn.set(c, false);
                addToBot(new ReturnToHandAction(c));
                if (c instanceof TriggerOnReturnInterface) {
                    ((TriggerOnReturnInterface) c).triggerOnReturn();
                }
                if (!returnedACard) returnedACard = true;
            }
        }
        if (returnedACard) {
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof TriggerOnReturnInterface) {
                    ((TriggerOnReturnInterface) p).triggerOnReturn();
                }
            }
        }
        this.isDone = true;
    }
}
