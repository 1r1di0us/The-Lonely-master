package lonelymod.actions;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import lonelymod.interfaces.TriggerOnAbilityInterface;
import lonelymod.orbs.BearBasicAbility;
import lonelymod.orbs.ByrdBasicAbility;
import lonelymod.orbs.SquirrelBasicAbility;
import lonelymod.orbs.WolfBasicAbility;
import lonelymod.powers.SquirrelPower;

public class CompanionBasicAbilityAction extends AbstractGameAction {

    public CompanionBasicAbilityAction() {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = AbstractGameAction.ActionType.SPECIAL;
    }

    @Override
    public void update() {
        // trigger cards in hand for triggerOnAbility
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof TriggerOnAbilityInterface) {
                ((TriggerOnAbilityInterface) c).triggerOnAbility(1);
            }
        }

        if (AbstractDungeon.player.hasPower(makeID("OmenPower"))) {
            AbstractDungeon.player.channelOrb((AbstractOrb)new ByrdBasicAbility());
        }
        else if (AbstractDungeon.player.hasRelic(makeID("BearCubPendant"))) {
            AbstractDungeon.player.channelOrb((AbstractOrb)new BearBasicAbility());
        }
        else if (AbstractDungeon.player.hasRelic(makeID("WolfPackPendant"))) {
            AbstractDungeon.player.channelOrb((AbstractOrb)new WolfBasicAbility());
        }
        else {
            if (!AbstractDungeon.player.hasPower(makeID("SquirrelPower"))) {
                if (AbstractDungeon.player.maxOrbs <= 0) {
                    AbstractDungeon.player.increaseMaxOrbSlots(1, false);
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SquirrelPower(AbstractDungeon.player)));
            }
            AbstractDungeon.player.channelOrb((AbstractOrb)new SquirrelBasicAbility());
        }
        this.isDone = true;
    }
}
