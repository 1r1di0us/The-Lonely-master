package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.potions.WaterFlask;

public class WaterFlaskAction extends AbstractGameAction {

    private int drawAmount;
    public WaterFlaskAction(int drawAmount) {
        this.drawAmount = drawAmount;
    }

    public void update() {
        addToTop(new DrawCardAction(this.drawAmount));
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.type.equals(AbstractCard.CardType.STATUS)) {
                addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.hand));
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c.type.equals(AbstractCard.CardType.STATUS)) {
                addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.drawPile));
            }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c.type.equals(AbstractCard.CardType.STATUS)) {
                addToTop(new ExhaustSpecificCardAction(c, AbstractDungeon.player.discardPile));
            }
        }
        this.isDone = true;
    }
}
