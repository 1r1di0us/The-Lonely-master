package lonelymod.actions;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;

public class AbandonAction extends AbstractGameAction {

    @Override
    public void update() {
        ArrayList<AbstractCard> realCards = new ArrayList<>(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.hand.getPurgeableCards()).group);
        realCards.removeIf(c -> StSLib.getMasterDeckEquivalent(c) == null);
        if (realCards.isEmpty()) {
            this.isDone = true;
            return;
        }
        AbstractCard cardToExhaust = realCards.get(AbstractDungeon.cardRandomRng.random(0, realCards.size() - 1));
        addToTop(new ExhaustSpecificCardAction(cardToExhaust, AbstractDungeon.player.hand));
        AbstractCard cardToPurge = StSLib.getMasterDeckEquivalent(cardToExhaust);
        AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(cardToPurge, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        AbstractDungeon.player.masterDeck.removeCard(cardToPurge);
        this.isDone = true;
    }
    
}
