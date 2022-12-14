package lonelymod.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import lonelymod.cards.Bravery;
import lonelymod.cards.Desperation;
import lonelymod.cards.Resolve;

public class LonelyAction extends AbstractGameAction {
    //boolean upgraded;

    public LonelyAction(boolean upgraded) {
        this.actionType = ActionType.SPECIAL;
        //this.upgraded = upgraded;
    }

    @Override
    public void update() {
        AbstractDungeon.player.releaseCard();
        ArrayList<AbstractCard> cardChoices = new ArrayList<>();
        AbstractCard cardToAdd;
        cardToAdd = new Bravery();
        cardToAdd.cost = 0;
        cardChoices.add(cardToAdd);
        cardToAdd = new Resolve();
        cardToAdd.cost = 0;
        cardChoices.add(cardToAdd);
        cardToAdd = new Desperation();
        cardToAdd.cost = 0;
        cardChoices.add(cardToAdd);
        //if (this.upgraded)
            //for (AbstractCard c : cardChoices)
                //c.upgrade();
        addToBot((AbstractGameAction)new ChooseOneAction(cardChoices));
        //after choosing a card, that card's onChooseAction activates
        this.isDone = true;
    }
    
}
