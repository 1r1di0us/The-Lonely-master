package lonelymod.actions;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import lonelymod.interfaces.TriggerOnAbilityInterface;
//import lonelymod.orbs.BearAttackAbility;
import lonelymod.orbs.ByrdAttackAbility;
import lonelymod.orbs.SquirrelAttackAbility;
import lonelymod.orbs.WolfAttackAbility;

public class CompanionAttackAbilityAction extends AbstractGameAction {

    public CompanionAttackAbilityAction() {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = AbstractGameAction.ActionType.SPECIAL;
    }

    @Override
    public void update() {
        // trigger cards in hand for triggerOnAbility
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof TriggerOnAbilityInterface) {
                ((TriggerOnAbilityInterface) c).triggerOnAbility(2);
            }
        }
        
        if (AbstractDungeon.player.hasPower(makeID("OmenPower"))) {
            AbstractDungeon.player.channelOrb((AbstractOrb)new ByrdAttackAbility());
        }
        //else if (AbstractDungeon.player.hasRelic(makeID("BearCubPendant"))) {
        //    AbstractDungeon.player.channelOrb((AbstractOrb)new BearAttackAbility());
        //}
        else if (AbstractDungeon.player.hasRelic(makeID("WolfPackPendant"))) {
            AbstractDungeon.player.channelOrb((AbstractOrb)new WolfAttackAbility());
        }
        else {
            AbstractDungeon.player.channelOrb((AbstractOrb)new SquirrelAttackAbility());
        }
        this.isDone = true;
    }
}
