package lonelymod.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

import lonelymod.cards.Bravery;
import lonelymod.cards.Desperation;
import lonelymod.cards.Resolve;

public class LonelyAction extends AbstractGameAction {
    boolean upgraded;

    public LonelyAction(boolean upgraded) {
        this.actionType = ActionType.SPECIAL;
        this.upgraded = upgraded;
    }

    @Override
    public void update() {
        ArrayList<AbstractCard> cardChoices = new ArrayList<>();
        cardChoices.add(new Bravery());
        cardChoices.add(new Desperation());
        cardChoices.add(new Resolve());
        if (this.upgraded)
        for (AbstractCard c : cardChoices)
            c.upgrade();
        addToBot((AbstractGameAction)new ChooseOneAction(cardChoices));
        //after choosing a card, that card's onChooseAction activates
        this.isDone = true;
    }
    
}
