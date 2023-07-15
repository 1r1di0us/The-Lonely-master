package lonelymod.patches;

import basemod.helpers.CardTags;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.LonelyMod;
import lonelymod.actions.SummonOutcastAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.cards.OmenCard;

@SpirePatch(
        clz=AbstractPlayer.class,
        method="applyStartOfCombatLogic"
)
public class StartOfCombatSummonPatch {

    public static void Postfix(AbstractPlayer __instance) {
        boolean needsCompanion = false;
        for (AbstractCard c : __instance.masterDeck.group) {
            if (c instanceof AbstractEasyCard) {
                if (CardTags.hasTag(c, AbstractEasyCard.Enums.COMPANION)) {
                    needsCompanion = true;
                    break;
                }
            }
        }
        if (needsCompanion && !(__instance.hasRelic(LonelyMod.makeID("BonesStomach"))) && !(__instance.hasRelic(LonelyMod.makeID("MeatsStomach")))) {
            /*if (__instance instanceof Ironclad) {
                //summon maniac
            } else if (__instance instanceof TheSilent) {
                //summon spy
            } else if (__instance instanceof Defect) {
                //summon mechanic
            } else if (__instance instanceof Watcher) {
                //summon oracle
            } else {*/
            AbstractDungeon.actionManager.addToBottom(new SummonOutcastAction());
            //}
        }
    }
}
