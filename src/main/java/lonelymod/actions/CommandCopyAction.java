package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import lonelymod.companions.AbstractCompanion;

import static lonelymod.LonelyMod.makeID;

public class CommandCopyAction extends AbstractGameAction {
    private static final UIStrings uistring = CardCrawlGame.languagePack.getUIString(makeID("CommandCopyMessage"));
    public static final String[] TEXT = uistring.TEXT;

    private AbstractCompanion c;

    private boolean upgraded;

    public CommandCopyAction(AbstractCompanion companion, boolean upgraded) {
        this.c = companion;
        this.upgraded = upgraded;
    }

    public void update() {
        if (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() >= 2 && (AbstractDungeon.actionManager.cardsPlayedThisCombat
                .get(AbstractDungeon.actionManager.cardsPlayedThisCombat
                        .size() - 2)).type == AbstractCard.CardType.ATTACK) { // this happens after command copy is played
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
        } else if (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() < 2) {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
        } else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[1], true));
        }
        this.isDone = true;
    }
}
