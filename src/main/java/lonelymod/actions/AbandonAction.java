package lonelymod.actions;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeID;

public class AbandonAction extends AbstractGameAction {
    public static final String ID = makeID("AbandonAction");
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;

    private ArrayList<AbstractCard> nonRealCards = new ArrayList<>();

    public AbandonAction() {
        this.amount = 1;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = AbstractGameAction.ActionType.EXHAUST;
    }

    @Override
    public void update() {
        //choose a card to remove from your hand, only allows removable cards to be chosen.
        if (this.duration == this.startDuration) {
            if (AbstractDungeon.player.hand.size() == 0) {
                this.isDone = true;
                return;
            }
            ArrayList<AbstractCard> realCards = new ArrayList<>(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.hand.getPurgeableCards()).group);
            realCards.removeIf(c -> StSLib.getMasterDeckEquivalent(c) == null);
            if (realCards.size() == 0) {
                this.isDone = true;
                return;
            }
            this.nonRealCards.addAll(AbstractDungeon.player.hand.group);
            this.nonRealCards.removeIf(realCards::contains);
            AbstractDungeon.player.hand.group.removeAll(nonRealCards);
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false, true, false, false, true);
            tickDuration();
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                AbstractDungeon.player.hand.moveToExhaustPile(c);
                AbstractCard cardToPurge = StSLib.getMasterDeckEquivalent(c);
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(cardToPurge, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.player.masterDeck.removeCard(cardToPurge);
            }
            returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
        }
        tickDuration();

        // this exhausts a random card in hand and removes it if possible
        /*if (AbstractDungeon.player.hand.isEmpty()) {
            this.isDone = true;
            return;
        }
        AbstractCard cardToExhaust = AbstractDungeon.player.hand.group.get(AbstractDungeon.cardRandomRng.random(0, AbstractDungeon.player.hand.group.size() - 1));
        addToTop(new ExhaustSpecificCardAction(cardToExhaust, AbstractDungeon.player.hand));

        ArrayList<AbstractCard> realCards = new ArrayList<>(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.hand.getPurgeableCards()).group);
        realCards.removeIf(c -> StSLib.getMasterDeckEquivalent(c) == null);
        if (realCards.isEmpty()) {
            this.isDone = true;
            return;
        }
        AbstractCard cardToPurge = null;
        for (int i = 0; i < realCards.size(); i++) {
            if (cardToExhaust.uuid == realCards.get(i).uuid) {
                cardToPurge = StSLib.getMasterDeckEquivalent(realCards.get(i));
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(cardToPurge, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.player.masterDeck.removeCard(cardToPurge);
                break;
            }
        }
        this.isDone = true;*/


        //this is the same thing as the second one but it can't target temp cards and unremovable cards.
        /*ArrayList<AbstractCard> realCards = new ArrayList<>(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.hand.getPurgeableCards()).group);
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
        this.isDone = true;*/
    }

    private void returnCards() {
        for (AbstractCard c : this.nonRealCards) {
            AbstractDungeon.player.hand.addToTop(c);
        }
        AbstractDungeon.player.hand.refreshHandLayout();
    }
    
}
