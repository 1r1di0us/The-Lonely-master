package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import lonelymod.companions.AbstractCompanion;
import lonelymod.powers.WildFormPower;

import static lonelymod.LonelyMod.makeID;

public class CallMoveAction extends AbstractGameAction {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("CallActionMessage"));
    public static final String[] TEXT = uiStrings.TEXT;

    private byte move;
    private AbstractCompanion currCompanion;

    public CallMoveAction(byte move, AbstractCompanion currCompanion) {
        this.actionType = ActionType.SPECIAL;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.move = move;
        this.currCompanion = currCompanion;
    }

    public void update() {
        if (currCompanion == null) {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
        }
        else {
            if (move == AbstractCompanion.DEFAULT) {
                currCompanion.callDefault();
            } else if (move == AbstractCompanion.NONE) {
                currCompanion.callNone();
            } else if (move == AbstractCompanion.UNKNOWN) {
                currCompanion.callUnknown();
            } else if (currCompanion.move.nextMove == AbstractCompanion.UNKNOWN) {
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[1], true));
            } else if (AbstractDungeon.player.hasPower(WildFormPower.POWER_ID)) {
                addToTop(new WildFormCallMoveAction(move, currCompanion));
            } else if (move == AbstractCompanion.ATTACK) {
                currCompanion.callMainMove(AbstractCompanion.ATTACK, true, true);
            } else if (move == AbstractCompanion.PROTECT) {
                currCompanion.callMainMove(AbstractCompanion.PROTECT, true, true);
            } else if (move == AbstractCompanion.SPECIAL) {
                currCompanion.callMainMove(AbstractCompanion.SPECIAL, true, true);
            } else {
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[2], true));
            }
        }
        this.isDone = true;
    }
}