package lonelymod.actions;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import lonelymod.cards.ABrilliantIdea;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.cards.Improvise;

public class PlanAction extends AbstractGameAction {
    public static final String ID = makeID("PlanAction");
    private static final UIStrings uistring = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uistring.TEXT;

    private float startingDuration;

    private AbstractEasyCard cardPlayed;

    private int improviseBlock;

    public PlanAction(int numCards) {
        this(numCards, null, 0);
    }

    public PlanAction(int numCards, AbstractEasyCard cardPlayed) {
        this(numCards, cardPlayed, 0);
    }

    public PlanAction(int numCards, AbstractEasyCard cardPlayed, int improviseBlock) {
        this.amount = numCards;
        this.cardPlayed = cardPlayed;
        this.improviseBlock = improviseBlock;
        if (AbstractDungeon.player.hasPower("TwoStepsAheadPower")) {
            AbstractDungeon.player.getPower("TwoStepsAheadPower").flash();;
            this.amount += AbstractDungeon.player.getPower("TwoStepsAheadPower").amount;
        } 
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
    }

    @Override
    public void update() {
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.isDone = true;
            return;
        }
        if (this.duration == this.startingDuration) {
            //for (AbstractPower p : AbstractDungeon.player.powers)
                //p.onScry(); 
            if (AbstractDungeon.player.discardPile.isEmpty()) {
                if (AbstractDungeon.player.hasPower("PlanBPower")) {
                    AbstractDungeon.player.getPower("PlanBPower").flash();
                    AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player,
                            AbstractDungeon.player.getPower("PlanBPower").amount, DamageType.THORNS), AttackEffect.SLASH_HEAVY));
                }
                this.isDone = true;
                return;
            } 
            CardGroup tmpGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            if (this.amount != -1) {
                for (int i = 0; i < Math.min(this.amount, AbstractDungeon.player.discardPile.size()); i++)
                    tmpGroup.addToTop(AbstractDungeon.player.discardPile.group
                            .get(AbstractDungeon.player.discardPile.size() - i - 1)); 
            } else {
                for (AbstractCard c : AbstractDungeon.player.discardPile.group)
                    tmpGroup.addToBottom(c); 
            } 
            AbstractDungeon.gridSelectScreen.open(tmpGroup, this.amount, true, TEXT[0]);
        } else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards)
                if (cardPlayed != null && cardPlayed instanceof ABrilliantIdea) {
                    AbstractDungeon.player.discardPile.moveToDeck(c, true);
                    AbstractDungeon.actionManager.addToBottom(new UpgradeSpecificCardAction(c));
                } else {
                    AbstractDungeon.player.discardPile.moveToDeck(c, true);
                    if (cardPlayed != null && cardPlayed instanceof Improvise) {
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, improviseBlock));
                    }
                }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        } else if (AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            if (AbstractDungeon.player.hasPower("PlanBPower")) {
                AbstractDungeon.player.getPower("PlanBPower").flash();
                AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player,
                        AbstractDungeon.player.getPower("PlanBPower").amount, DamageType.THORNS), AttackEffect.SLASH_HEAVY));
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group)
            if (c instanceof AbstractEasyCard) {
                AbstractEasyCard ce = (AbstractEasyCard)c;
                ce.triggerOnPlan();
            }
        tickDuration();
    }
}