package lonelymod.actions;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import lonelymod.interfaces.TriggerOnAbilityInterface;
//import lonelymod.orbs.BearSpecialAbility;
import lonelymod.orbs.ByrdSpecialAbility;
import lonelymod.orbs.SquirrelSpecialAbility;
import lonelymod.orbs.WolfSpecialAbility;

public class CompanionSpecialAbilityAction extends AbstractGameAction {

    public CompanionSpecialAbilityAction() {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = AbstractGameAction.ActionType.SPECIAL;
    }

    @Override
    public void update() {
        // trigger cards in hand for triggerOnAbility
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof TriggerOnAbilityInterface) {
                ((TriggerOnAbilityInterface) c).triggerOnAbility(4);
            }
        }

        if (AbstractDungeon.player.hasPower(makeID("OmenPower"))) {
            AbstractDungeon.player.channelOrb((AbstractOrb)new ByrdSpecialAbility());
        }
        //else if (AbstractDungeon.player.hasRelic(makeID("BearCubPendant"))) {
        //    AbstractDungeon.player.channelOrb((AbstractOrb)new BearSpecialAbility());
        //}
        else if (AbstractDungeon.player.hasRelic(makeID("WolfPackPendant"))) {
            AbstractDungeon.player.channelOrb((AbstractOrb)new WolfSpecialAbility());
        }
        else {
            AbstractDungeon.player.channelOrb((AbstractOrb)new SquirrelSpecialAbility());
        }
        this.isDone = true;
    }
}
