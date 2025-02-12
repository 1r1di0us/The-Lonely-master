package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ClearThoughtsFollowUpAction extends AbstractGameAction {
    public void update() {
        AbstractDungeon.actionManager.addToTop(new WaitAction(0.4F));
        tickDuration();
        if (this.isDone)
            for (AbstractCard c : DrawCardAction.drawnCards) {
                if (c.type == AbstractCard.CardType.STATUS) {
                    addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand, true));
                }
            }
    }

}
