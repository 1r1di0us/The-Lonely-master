package lonelymod.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import lonelymod.cards.Bravery;
import lonelymod.cards.Desperation;
import lonelymod.cards.Resolve;

public class DesperationAction extends AbstractGameAction {
    ArrayList<AbstractCard> cardChoices;

    public DesperationAction(ArrayList<AbstractCard> choices) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.cardChoices = choices;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.chooseOneOpen(this.cardChoices);
            tickDuration();
            return;
        }
        tickDuration();
    }
}
