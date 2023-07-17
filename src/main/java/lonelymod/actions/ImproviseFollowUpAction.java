package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.powers.ImprovisePower;

public class ImproviseFollowUpAction extends AbstractGameAction {
    private int blockAmount;

    public ImproviseFollowUpAction(int blockAmount) {
        this.blockAmount = blockAmount;
    }
    public void update() {
        AbstractDungeon.actionManager.addToTop(new WaitAction(0.4F));
        tickDuration();
        if (this.isDone)
            for (AbstractCard c : DrawCardAction.drawnCards) {
                addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ImprovisePower(AbstractDungeon.player, c, blockAmount)));
            }
    }
}
