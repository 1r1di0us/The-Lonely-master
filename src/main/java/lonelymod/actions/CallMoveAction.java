package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
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
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("CallMoveMessage"));
    public static final String[] TEXT = uiStrings.TEXT;

    private final byte move;
    private final AbstractCompanion currCompanion;
    private boolean triggerPowers;
    private boolean flash;
    private boolean makeIntent;

    public CallMoveAction(byte move, AbstractCompanion currCompanion, boolean triggerPowers, boolean flash, boolean makeIntent) {
        this.actionType = ActionType.SPECIAL;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.move = move;
        this.currCompanion = currCompanion;
        this.triggerPowers = triggerPowers; //set triggerPowers to false to denote no triggering powers under any circumstances
        this.flash = flash;
        this.makeIntent = makeIntent;
        // set triggerPowers to false when calling DEFAULT, UNKNOWN, or NONE
    }
    public CallMoveAction(byte move, AbstractCompanion currCompanion, boolean triggerPowers, boolean silentCall) { this(move, currCompanion, triggerPowers, !silentCall, !silentCall); }
    public CallMoveAction(byte move, AbstractCompanion currCompanion, boolean triggerPowers) { this(move, currCompanion, triggerPowers, true, true); }
    public CallMoveAction(byte move, AbstractCompanion currCompanion) {
        this(move, currCompanion, true, true, true);
    }

    public void update() {
        if (currCompanion == null) {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
        }
        else {
            byte prevMove = currCompanion.nextMove;
            if (prevMove == AbstractCompanion.UNKNOWN && move != AbstractCompanion.DEFAULT) {
                triggerPowers = false; // unknown move (frenzy) stops calling of moves
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[1], true));
            } else if (AbstractDungeon.player.hasPower(WildFormPower.POWER_ID) && triggerPowers) {
                AbstractDungeon.player.getPower(WildFormPower.POWER_ID).flash();
                if (AbstractDungeon.player.getPower(WildFormPower.POWER_ID).amount > 1) {//Multiple wild forms means lets speed things up
                    for (int i = 0; i < AbstractDungeon.player.getPower(WildFormPower.POWER_ID).amount - 1; i++) {
                        //addToBot(new CompanionTakeTurnAction(false));
                        addToBot(new PerformMoveAction(prevMove, currCompanion));
                        addToBot(new WaitAction(0.1F));
                    }
                }
                addToBot(new CompanionTakeTurnAction(false, move, triggerPowers)); // then call one more time, which calls the new move at the end
                triggerPowers = false; // trigger powers later.
            } else if (move > AbstractCompanion.NONE || move < AbstractCompanion.DEFAULT) { // aka the move is not real
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[2], true));
            } else {
                currCompanion.callMove(move, flash, makeIntent);
            }
            if (triggerPowers) {
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