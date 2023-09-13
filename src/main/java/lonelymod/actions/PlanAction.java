package lonelymod.actions;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.UpgradeSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import lonelymod.cards.Genius;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.interfaces.TriggerOnPlanInterface;
import lonelymod.powers.PlanBPower;
import lonelymod.powers.TwoStepsAheadPower;

public class PlanAction extends AbstractGameAction {
    public static final String ID = makeID("PlanAction");
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;

    private final float startingDuration;
    private final AbstractEasyCard cardPlayed;
    private int numCardsSelected = 0;

    public PlanAction(int numCards) {
        this(numCards, null);
    }

    public PlanAction(int numCards, AbstractEasyCard cardPlayed) {
        this.amount = numCards;
        this.cardPlayed = cardPlayed;
        if (AbstractDungeon.player.hasPower(TwoStepsAheadPower.POWER_ID)) {
            AbstractDungeon.player.getPower(TwoStepsAheadPower.POWER_ID).flash();
            this.amount += AbstractDungeon.player.getPower(TwoStepsAheadPower.POWER_ID).amount;
        } 
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
    }

    @Override
    public void update() {
        //for (AbstractPower p : AbstractDungeon.player.powers) {
        //  this.amount = p.onPlan(this.amount);
        //}
        if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.isDone = true;
            return;
        }
        if (this.duration == this.startingDuration) {
            if (AbstractDungeon.player.discardPile.isEmpty()) {
                /*if (AbstractDungeon.player.hasPower(makeID("PlanBPower"))) {
                    AbstractDungeon.player.getPower(makeID("PlanBPower")).flash();
                    addToTop(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player,
                            AbstractDungeon.player.getPower(makeID("PlanBPower")).amount, DamageType.THORNS), AttackEffect.SLASH_HEAVY));
                }*/
                this.isDone = true;
                triggerPlanActions();
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
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                if (c instanceof TriggerOnPlanInterface) {
                    ((TriggerOnPlanInterface) c).triggerOnPlan(true);
                }
                AbstractDungeon.player.discardPile.moveToDeck(c, true);
                if (cardPlayed != null && cardPlayed instanceof Genius)
                    addToTop(new UpgradeSpecificCardAction(c));
            }
            numCardsSelected = AbstractDungeon.gridSelectScreen.selectedCards.size();
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        tickDuration();
        if (this.isDone)
            triggerPlanActions();
    }

    private void triggerPlanActions() {
        //for (AbstractCard c : AbstractDungeon.player.drawPile.group) {}
        //for (AbstractCard c : AbstractDungeon.player.discardPile.group) {}
        //for (AbstractCard c : AbstractDungeon.player.hand.group) {}
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) { //this was for now Deprecated Part of the Plan, which is drawn when you plan
            if (c instanceof TriggerOnPlanInterface) {
                ((TriggerOnPlanInterface) c).triggerOnPlan(false);
            }
        }
        //for (AbstractPower p : AbstractDungeon.player.powers) {
        //  p.postPlan(selectedCards.size());
        //}
        if (AbstractDungeon.player.hasPower(PlanBPower.POWER_ID) && this.amount - numCardsSelected > 0) {
            ((PlanBPower) AbstractDungeon.player.getPower(PlanBPower.POWER_ID)).onPlan(this.amount - numCardsSelected);
        }
/*        if (this.isDone && AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            if (AbstractDungeon.player.hasPower(PlanBPower.POWER_ID)) {
                AbstractDungeon.player.getPower(PlanBPower.POWER_ID).onPlan();
            }
        }*/
    }
}
