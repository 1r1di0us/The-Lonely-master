package lonelymod.patches;

import basemod.helpers.CardTags;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.LonelyCharacter;
import lonelymod.actions.SummonCompanionAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.companions.*;
import lonelymod.relics.BonesStomach;
import lonelymod.relics.MeatsStomach;

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
        if (needsCompanion && !(__instance.hasRelic(BonesStomach.ID)) && !(__instance.hasRelic(MeatsStomach.ID))) {
            //default = maniac
            AbstractCompanion companion = new Maniac();
            if (__instance instanceof Ironclad) {
                //summon maniac
                companion = new Maniac();
            } else if (__instance instanceof TheSilent) {
                //summon spy
                companion = new Spy();
            } else if (__instance instanceof Defect) {
                //summon mechanic
                companion = new Mechanic();
            } else if (__instance instanceof Watcher) {
                //summon oracle
                companion = new Oracle();
            } else if (__instance instanceof LonelyCharacter) {
                //summon outcast
                companion =  new Outcast();
            }
            AbstractDungeon.actionManager.addToBottom(new SummonCompanionAction(companion));
        }
    }
}
