package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MentalOverloadAction extends AbstractGameAction {
    private AbstractCard card;
    private int baseCost;
    private int difference;
    // The point of this is to delay changing mental Overload's cost until after stuff has already happened

    public MentalOverloadAction(AbstractCard card, int baseCost, int difference) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        this.source = AbstractDungeon.player;
        this.card = card;
        this.baseCost = baseCost;
        this.difference = difference;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            card.setCostForTurn(baseCost - (AbstractDungeon.player.hand.size() - difference));
        }

        this.isDone = true;
    }

}