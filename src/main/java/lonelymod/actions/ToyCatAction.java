/*
package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.relics.ToyCat;

public class ToyCatAction extends AbstractGameAction {
    private AbstractPlayer p;
    private AbstractCard card;
    public ToyCatAction(AbstractCard card) {
        this.p = AbstractDungeon.player;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.card = card;
    }

    public void update() {
        //i've probably sorely mistaken how this should work.
        */
/*if (card == null) {
            if (p.hasRelic(ToyCat.ID)) {
                this.card = ToyCat.getCard();
                p.masterDeck.addToRandomSpot(this.card);
                p.hand.addToBottom(this.card);
                this.isDone = true;
            } else {
                this.isDone = true;
            }
        }*//*

        for (AbstractCard c : p.drawPile.group) {
            if (c.uuid == card.uuid) {
                p.drawPile.moveToHand(card);
                this.isDone = true;
                break;
            }
        }
        if (!this.isDone) {
            for (AbstractCard c : p.discardPile.group) {
                if (c.uuid == card.uuid) {
                    p.discardPile.moveToHand(card);
                    this.isDone = true;
                    break;
                }
            }
        }
        if (!this.isDone) {
            for (AbstractCard c : p.exhaustPile.group) {
                if (c.uuid == card.uuid) {
                    p.exhaustPile.moveToHand(card);
                    this.isDone = true;
                    break;
                }
            }
        }
        if (!this.isDone) {
            for (AbstractCard c : p.limbo.group) {
                if (c.uuid == card.uuid) {
                    p.limbo.moveToHand(card);
                    this.isDone = true;
                    break;
                }
            }
        }
        if (!this.isDone) {
            for (AbstractCard c : p.hand.group) {
                if (c.uuid == card.uuid) {
                    p.hand.moveToHand(card); //screw you
                    this.isDone = true;
                    break;
                }
            }
        }
        */
/*if (!this.isDone) {
            //hopefully this doesn't happen
            this.card = ToyCat.getCard();
            p.masterDeck.addToRandomSpot(this.card);
            p.hand.addToBottom(this.card);
            this.isDone = true;
        }*//*

    }
}
*/
