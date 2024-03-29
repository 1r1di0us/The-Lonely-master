package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.powers.AdaptPower;

public class AdaptFollowUpAction extends AbstractGameAction {
    private final int blockAmount;

    public AdaptFollowUpAction(int blockAmount) {
        this.blockAmount = blockAmount;
    }
    public void update() {
        addToTop(new WaitAction(0.4F));
        tickDuration();
        if (this.isDone)
            for (AbstractCard c : DrawCardAction.drawnCards) {
                addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AdaptPower(AbstractDungeon.player, c, blockAmount)));
            }
    }
}
