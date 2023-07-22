package lonelymod.actions;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class SetAutoplayAction extends AbstractGameAction {

    private AbstractCard card;
    public SetAutoplayAction(AbstractCard card) {
        this.card = card;
    }

    public void update() {
        AutoplayField.autoplay.set(card, true);
        this.isDone = true;
    }
}
