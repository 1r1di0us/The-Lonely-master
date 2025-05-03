package lonelymod.patches;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.LonelyCharacter;
import lonelymod.actions.SummonCompanionAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.companions.*;
import lonelymod.fields.CompanionField;
import lonelymod.relics.BonesStomach;
import lonelymod.relics.MeatsStomach;

@SpirePatch(clz = CardGroup.class, method = "addToTop", paramtypez = AbstractCard.class)
@SpirePatch(clz = CardGroup.class, method = "addToBottom", paramtypez = AbstractCard.class)
@SpirePatch(clz = CardGroup.class, method = "addToRandomSpot", paramtypez = AbstractCard.class)
public class CreateCardSummonPatch {
    @SpirePrefixPatch
    public static void summonCompanionWithCard(CardGroup __instance, AbstractCard c) {
        if ((__instance.type == CardGroup.CardGroupType.HAND ||
                __instance.type == CardGroup.CardGroupType.DISCARD_PILE ||
                __instance.type == CardGroup.CardGroupType.DRAW_PILE) &&
                StSLib.getMasterDeckEquivalent(c) == null) {
            if (c.hasTag(AbstractEasyCard.Enums.COMPANION) &&
                    CompanionField.currCompanion.get(AbstractDungeon.player) == null &&
                    !(AbstractDungeon.player.hasRelic(BonesStomach.ID) || AbstractDungeon.player.hasRelic(MeatsStomach.ID))) {
                //default = maniac
                AbstractCompanion companion = new Maniac();
                if (AbstractDungeon.player instanceof Ironclad) {
                    //summon maniac
                    companion = new Maniac();
                } else if (AbstractDungeon.player instanceof TheSilent) {
                    //summon spy
                    companion = new Spy();
                } else if (AbstractDungeon.player instanceof Defect) {
                    //summon mechanic
                    companion = new Mechanic();
                } else if (AbstractDungeon.player instanceof Watcher) {
                    //summon oracle
                    companion = new Oracle();
                } else if (AbstractDungeon.player instanceof LonelyCharacter) {
                    //summon outcast
                    companion =  new Outcast();
                }
                AbstractDungeon.actionManager.addToBottom(new SummonCompanionAction(companion));
            }
        }
    }

}
