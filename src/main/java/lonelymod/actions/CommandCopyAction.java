package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.companions.AbstractCompanion;

public class CommandCopyAction extends AbstractGameAction {
    private AbstractCompanion c;

    private boolean upgraded;

    public CommandCopyAction(AbstractCompanion companion, boolean upgraded) {
        this.c = companion;
        this.upgraded = upgraded;
    }

    public void update() {
        if (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() >= 2 && (AbstractDungeon.actionManager.cardsPlayedThisCombat
                .get(AbstractDungeon.actionManager.cardsPlayedThisCombat
                        .size() - 2)).type == AbstractCard.CardType.ATTACK) {
            addToTop(new CallMoveAction(AbstractCompanion.ATTACK, this.c));
        } else if (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() >= 2 && (AbstractDungeon.actionManager.cardsPlayedThisCombat
                .get(AbstractDungeon.actionManager.cardsPlayedThisCombat
                        .size() - 2)).type == AbstractCard.CardType.SKILL) {
            addToTop(new CallMoveAction(AbstractCompanion.PROTECT, this.c));
        } else if (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() >= 2 && (AbstractDungeon.actionManager.cardsPlayedThisCombat
                .get(AbstractDungeon.actionManager.cardsPlayedThisCombat
                        .size() - 2)).type == AbstractCard.CardType.POWER
                && this.upgraded) {
            addToTop(new CallMoveAction(AbstractCompanion.SPECIAL, this.c));
        }
        this.isDone = true;
    }
}
