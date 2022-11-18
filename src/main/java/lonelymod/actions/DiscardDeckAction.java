package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DiscardDeckAction extends AbstractGameAction{

    @Override
    public void update() {
        while (!AbstractDungeon.player.drawPile.isEmpty()) {
            AbstractCard card = AbstractDungeon.player.drawPile.getTopCard();
            AbstractDungeon.player.drawPile.moveToDiscardPile(card);
        }
        this.isDone = true;
    }
    
}
