package lonelymod.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import lonelymod.cards.Bravery;
import lonelymod.cards.Desperation;
import lonelymod.cards.Resolve;
import lonelymod.powers.LonelyPower;

public class LonelyAction extends AbstractGameAction {

    public LonelyAction(int amount) {
        this.amount = amount;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        AbstractDungeon.player.releaseCard();
        ArrayList<AbstractCard> cardChoices = new ArrayList<>();
        AbstractCard cardToAdd;
        cardToAdd = new Bravery();
        cardToAdd.cost = 0;
        cardChoices.add(cardToAdd);
        cardToAdd = new Desperation();
        cardToAdd.cost = 0;
        cardChoices.add(cardToAdd);
        cardToAdd = new Resolve();
        cardToAdd.cost = 0;
        cardChoices.add(cardToAdd);
        addToTop(new ChooseOneAction(cardChoices));
        //after choosing a card, that card's onChooseAction activates
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LonelyPower(AbstractDungeon.player, this.amount)));
        this.isDone = true;
    }
    
}
