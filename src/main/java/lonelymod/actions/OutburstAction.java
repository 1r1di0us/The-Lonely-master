package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class OutburstAction extends AbstractGameAction {
  public static final String[] TEXT = (CardCrawlGame.languagePack.getUIString("WishAction")).TEXT;
  
  private final AbstractPlayer player;
  
  private final int playAmt;
  private final boolean upgraded;
  private boolean attackInDiscard = false;

    public OutburstAction(int numberOfCards, boolean upp) {
    this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    this.player = AbstractDungeon.player;
    this.playAmt = numberOfCards;
    this.upgraded = upp;
  }
  
  public void update() {
    if (!this.upgraded) {
        if (!this.player.discardPile.isEmpty()) {
            for (int i = 0; i < this.player.discardPile.group.size(); i++) {
                if (this.player.discardPile.group.get(i).type == CardType.ATTACK) {
                    attackInDiscard = true;
                    //discard pile is not empty and contains at least 1 attack
                    break;
                }
            }
        }
        if (!attackInDiscard) {
            this.isDone = true;
            return;
        }
        AbstractCard c = this.player.discardPile.getRandomCard(CardType.ATTACK, true);
        c.exhaust = true;
        AbstractDungeon.player.discardPile.group.remove(c);
        (AbstractDungeon.getCurrRoom()).souls.remove(c);
        addToBot(new NewQueueCardAction(c, true, false, true));
        for (int i = 0; i < this.playAmt - 1; i++) {
            AbstractCard tmp = c.makeSameInstanceOf();
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = c.current_x;
            tmp.current_y = c.current_y;
            tmp.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
            tmp.target_y = Settings.HEIGHT / 2.0F;
            tmp.purgeOnUse = true;
            addToBot(new NewQueueCardAction(tmp, true, false, true));
        }
        AbstractDungeon.player.hand.refreshHandLayout();
        this.isDone = true;
    } else {
        if (this.duration == this.startDuration) {
            if (!this.player.discardPile.isEmpty()) {
                for (int i = 0; i < this.player.discardPile.group.size(); i++) {
                    if (this.player.discardPile.group.get(i).type == CardType.ATTACK) {
                        attackInDiscard = true;
                        //discard pile is not empty and contains at least 1 attack
                        break;
                    }
                }
            }
            if (!attackInDiscard) {
                this.isDone = true;
                return;
            }
            CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : this.player.discardPile.group)
                if (c.type == CardType.ATTACK)
                    temp.addToTop(c);

            if (temp.size() == 1) { //only one attack in discard
                AbstractCard c = temp.getTopCard();
                c.exhaust = true;
                AbstractDungeon.player.discardPile.group.remove(c);
                (AbstractDungeon.getCurrRoom()).souls.remove(c);
                addToBot(new NewQueueCardAction(c, true, false, true));
                for (int i = 0; i < this.playAmt - 1; i++) {
                    AbstractCard tmp = c.makeSameInstanceOf();
                    AbstractDungeon.player.limbo.addToBottom(tmp);
                    tmp.current_x = c.current_x;
                    tmp.current_y = c.current_y;
                    tmp.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                    tmp.target_y = Settings.HEIGHT / 2.0F;
                    tmp.purgeOnUse = true;
                    addToBot(new NewQueueCardAction(tmp, true, false, true));
                }
                this.isDone = true;
                return;
            }
            //otherwise...
            temp.sortAlphabetically(true);
            temp.sortByRarityPlusStatusCardType(false);
            AbstractDungeon.gridSelectScreen.open(temp, 1, TEXT[0], false);
            tickDuration();
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                c.exhaust = true;
                AbstractDungeon.player.discardPile.group.remove(c);
                (AbstractDungeon.getCurrRoom()).souls.remove(c);
                addToBot(new NewQueueCardAction(c, true, false, true));
                for (int i = 0; i < this.playAmt - 1; i++) {
                    AbstractCard tmp = c.makeSameInstanceOf();
                    AbstractDungeon.player.limbo.addToBottom(tmp);
                    tmp.current_x = c.current_x;
                    tmp.current_y = c.current_y;
                    tmp.target_x = Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                    tmp.target_y = Settings.HEIGHT / 2.0F;
                    tmp.purgeOnUse = true;
                    addToBot(new NewQueueCardAction(tmp, true, false, true));
                }
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
        }
        tickDuration();
    }
  }
}
