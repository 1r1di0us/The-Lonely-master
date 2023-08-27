package lonelymod.patches;

import basemod.helpers.CardTags;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.LonelyCharacter;
import lonelymod.LonelyMod;
import lonelymod.actions.*;
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
            if (__instance instanceof Ironclad) {
                //summon maniac
                AbstractDungeon.actionManager.addToBottom(new SummonManiacAction());
            } else if (__instance instanceof TheSilent) {
                //summon spy
                AbstractDungeon.actionManager.addToBottom(new SummonSpyAction());
            } else if (__instance instanceof Defect) {
                //summon mechanic
                AbstractDungeon.actionManager.addToBottom(new SummonMechanicAction());
            } else if (__instance instanceof Watcher) {
                //summon oracle
                AbstractDungeon.actionManager.addToBottom(new SummonOracleAction());
            } else if (__instance instanceof LonelyCharacter) {
                //summon outcast
                AbstractDungeon.actionManager.addToBottom(new SummonOutcastAction());
            } else {
                //default = maniac
                AbstractDungeon.actionManager.addToBottom(new SummonManiacAction());
            }
        }
    }
}
