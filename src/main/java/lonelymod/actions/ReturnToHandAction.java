package lonelymod.actions;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostForTurnAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ReturnToHandAction extends AbstractGameAction {

    private AbstractCard card;

    public ReturnToHandAction(AbstractCard card) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.card = card;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.player.drawPile.contains(this.card) && AbstractDungeon.player.hand.size() < 10) {
                AbstractDungeon.player.hand.addToHand(this.card);
                if (AbstractDungeon.player.hasPower(makeID("MuscleMemoryPower"))) {
                    AbstractDungeon.actionManager.addToBottom(new ReduceCostForTurnAction(this.card, AbstractDungeon.player.getPower(makeID("MuscleMemoryPower")).amount));
                }
                this.card.unhover();
                this.card.setAngle(0.0F, true);
                this.card.lighten(false);
                this.card.drawScale = 0.12F;
                this.card.targetDrawScale = 0.75F;
                this.card.applyPowers();
                AbstractDungeon.player.drawPile.removeCard(this.card);
            }
            else if (AbstractDungeon.player.discardPile.contains(this.card) && AbstractDungeon.player.hand.size() < 10) {
                AbstractDungeon.player.hand.addToHand(this.card);
                if (AbstractDungeon.player.hasPower(makeID("MuscleMemoryPower"))) {
                    AbstractDungeon.actionManager.addToBottom(new ReduceCostForTurnAction(card, AbstractDungeon.player.getPower(makeID("MuscleMemoryPower")).amount));
                }
                this.card.unhover();
                this.card.setAngle(0.0F, true);
                this.card.lighten(false);
                this.card.drawScale = 0.12F;
                this.card.targetDrawScale = 0.75F;
                this.card.applyPowers();
                AbstractDungeon.player.discardPile.removeCard(this.card);
            } 
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.glowCheck();
        }
        tickDuration();
        this.isDone = true;
    }
}
