package lonelymod.actions;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import lonelymod.cards.AbstractEasyCard;
//import lonelymod.orbs.BearProtectAbility;
import lonelymod.orbs.ByrdProtectAbility;
import lonelymod.orbs.SquirrelProtectAbility;
import lonelymod.orbs.WolfProtectAbility;

public class CompanionProtectAbilityAction extends AbstractGameAction {

    public CompanionProtectAbilityAction() {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = AbstractGameAction.ActionType.SPECIAL;
    }

    @Override
    public void update() {
        // trigger cards in hand for triggerOnAbility
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c instanceof AbstractEasyCard) {
                AbstractEasyCard ce = (AbstractEasyCard) c;
                ce.triggerOnAbility(3);
            }
        }

        if (AbstractDungeon.player.hasPower(makeID("OmenPower"))) {
            AbstractDungeon.player.channelOrb((AbstractOrb)new ByrdProtectAbility());
        }
        //else if (AbstractDungeon.player.hasRelic(makeID("BearCubPendant"))) {
        //    AbstractDungeon.player.channelOrb((AbstractOrb)new BearProtectAbility());
        //}
        else if (AbstractDungeon.player.hasRelic(makeID("WolfPackPendant"))) {
            AbstractDungeon.player.channelOrb((AbstractOrb)new WolfProtectAbility());
        }
        else {
            AbstractDungeon.player.channelOrb((AbstractOrb)new SquirrelProtectAbility());
        }
        this.isDone = true;
    }
}
