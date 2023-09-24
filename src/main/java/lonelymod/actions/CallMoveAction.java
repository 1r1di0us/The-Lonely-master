package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import lonelymod.cards.VentFrustration;
import lonelymod.companions.AbstractCompanion;
import lonelymod.interfaces.TriggerOnCallMoveInterface;
import lonelymod.powers.WildFormPower;

import static lonelymod.LonelyMod.makeID;

public class CallMoveAction extends AbstractGameAction {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("CallActionMessage"));
    public static final String[] TEXT = uiStrings.TEXT;

    private final byte move;
    private final AbstractCompanion currCompanion;

    public CallMoveAction(byte move, AbstractCompanion currCompanion) {
        this.actionType = ActionType.SPECIAL;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.move = move;
        this.currCompanion = currCompanion;
    }

    public void update() {
        boolean triggerPowers = false;
        if (currCompanion == null) {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
        }
        else {
            if (move == AbstractCompanion.DEFAULT) {
                currCompanion.callDefault();
            } else if (move == AbstractCompanion.UNKNOWN) {
                currCompanion.callUnknown();
            } else if (currCompanion.move.nextMove == AbstractCompanion.UNKNOWN) {
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[1], true));
            } else if (move == AbstractCompanion.NONE) {
                currCompanion.callNone(); //doesn't activate wild form because it is used when omen is summoned
            } else if (AbstractDungeon.player.hasPower(WildFormPower.POWER_ID)) {
                AbstractDungeon.player.getPower(WildFormPower.POWER_ID).flash();
                addToTop(new WildFormCallMoveAction(move, currCompanion)); //this happens second, need to perform last move before you call this move.
                for (int i = 0; i < AbstractDungeon.player.getPower(WildFormPower.POWER_ID).amount; i++)
                    addToTop(new CompanionTakeTurnAction(false)); //this happens first.
                triggerPowers = true;
            } else if (move == AbstractCompanion.ATTACK) {
                currCompanion.callMainMove(AbstractCompanion.ATTACK, true, true);
                triggerPowers = true;
            } else if (move == AbstractCompanion.PROTECT) {
                currCompanion.callMainMove(AbstractCompanion.PROTECT, true, true);
                triggerPowers = true;
            } else if (move == AbstractCompanion.SPECIAL) {
                currCompanion.callMainMove(AbstractCompanion.SPECIAL, true, true);
                triggerPowers = true;
            } else {
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[2], true));
            }
            if (triggerPowers) {
                for (AbstractPower p : currCompanion.powers)
                    if (p instanceof TriggerOnCallMoveInterface)
                        ((TriggerOnCallMoveInterface) p).triggerOnCallMove(move, currCompanion.nextMove);
                for (AbstractPower p : AbstractDungeon.player.powers)
                    if (p instanceof TriggerOnCallMoveInterface)
                        ((TriggerOnCallMoveInterface) p).triggerOnCallMove(move, currCompanion.nextMove);
                VentFrustration.movesCalledThisTurn++;
                for (AbstractCard c : AbstractDungeon.player.hand.group)
                    if (c instanceof TriggerOnCallMoveInterface)
                        ((TriggerOnCallMoveInterface) c).triggerOnCallMove(move, currCompanion.nextMove);
            }
        }
        this.isDone = true;
    }
}