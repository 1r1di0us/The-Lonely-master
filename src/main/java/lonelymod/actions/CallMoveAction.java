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
    private int triggerPowers;

    public CallMoveAction(byte move, AbstractCompanion currCompanion, int triggerPowers) {
        this.actionType = ActionType.SPECIAL;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.move = move;
        this.currCompanion = currCompanion;
        this.triggerPowers = triggerPowers; //set triggerPowers to -1 to denote no triggering powers under any circumstances
        // triggerPowers being set to -1 is no longer being used, use AbstractCompanion.refreshMove instead
        // But I will leave this functionality in.
    }

    public CallMoveAction(byte move, AbstractCompanion currCompanion) {
        this(move, currCompanion, 0);
    }

    public void update() {
        if (currCompanion == null) {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
        }
        else {
            byte prevMove = currCompanion.nextMove;
            if (move == AbstractCompanion.DEFAULT) {
                currCompanion.callDefault();
            } else if (move == AbstractCompanion.UNKNOWN) {
                currCompanion.callUnknown();
            } else if (currCompanion.move.nextMove == AbstractCompanion.UNKNOWN) {
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[1], true));
            } else if (move == AbstractCompanion.NONE) {
                currCompanion.callNone(); //doesn't activate wild form because it is used when omen is summoned
            } else if (AbstractDungeon.player.hasPower(WildFormPower.POWER_ID) && triggerPowers != -1) {
                AbstractDungeon.player.getPower(WildFormPower.POWER_ID).flash();
                addToTop(new WildFormCallMoveAction(move, currCompanion)); //this happens second, need to perform last move before you call this move.
                for (int i = 0; i < AbstractDungeon.player.getPower(WildFormPower.POWER_ID).amount; i++)
                    addToTop(new CompanionTakeTurnAction(false)); //this happens first.
                if (triggerPowers == 0) triggerPowers = 1; //if we are ok with triggering powers do it
            } else if (move == AbstractCompanion.ATTACK || move == AbstractCompanion.PROTECT || move == AbstractCompanion.SPECIAL) {
                currCompanion.callMainMove(move, true, true);
                if (triggerPowers == 0) triggerPowers = 1; //if we are ok with triggering powers do it
            } else {
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[2], true));
            }
            if (triggerPowers == 1) {
                for (AbstractPower p : currCompanion.powers)
                    if (p instanceof TriggerOnCallMoveInterface)
                        ((TriggerOnCallMoveInterface) p).triggerOnCallMove(move, prevMove);
                for (AbstractPower p : AbstractDungeon.player.powers)
                    if (p instanceof TriggerOnCallMoveInterface)
                        ((TriggerOnCallMoveInterface) p).triggerOnCallMove(move, prevMove);
                VentFrustration.movesCalledThisTurn++;
                for (AbstractCard c : AbstractDungeon.player.hand.group)
                    if (c instanceof TriggerOnCallMoveInterface)
                        ((TriggerOnCallMoveInterface) c).triggerOnCallMove(move, prevMove);
            }
        }
        this.isDone = true;
    }
}