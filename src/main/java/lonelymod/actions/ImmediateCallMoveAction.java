package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import lonelymod.cards.VentFrustration;
import lonelymod.companions.AbstractCompanion;
import lonelymod.interfaces.TriggerOnCallMoveInterface;

import static lonelymod.LonelyMod.makeID;

public class ImmediateCallMoveAction extends AbstractGameAction {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("CallMoveMessage"));
    public static final String[] TEXT = uiStrings.TEXT; // steal text from CallMoveAction

    private final byte move;
    private final AbstractCompanion currCompanion;
    private boolean triggerPowers;
    private boolean silentCall;
    // used immediately after CompanionTakeTurnAction when used by Primal, Special Sauce, or Wild Form.
    public ImmediateCallMoveAction(byte move, AbstractCompanion currCompanion, boolean triggerPowers, boolean silentCall) {
        this.move = move;
        this.currCompanion = currCompanion;
        this.triggerPowers = triggerPowers;
        this.silentCall = silentCall;
    }

    public void update() {
        byte prevMove = currCompanion.nextMove;
        if (move > AbstractCompanion.NONE || move < AbstractCompanion.DEFAULT) { // aka the move is not real
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[2], true));
        } else { // very simple, you could abuse it with default, unknown and none moves potentially but just don't?
            if (silentCall) currCompanion.callMove(move, false, true); // I think we NEED to make Intent here
            else currCompanion.callMove(move, true, true);
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
        this.isDone = true;
    }
}
