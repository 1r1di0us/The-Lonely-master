package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import lonelymod.fields.CompanionField;
import lonelymod.powers.ManiacPower;

@SpirePatch(
        clz=AbstractPlayer.class,
        method="damage"
)
public class OnPlayerLoseHpPatch {
    public static void Postfix(AbstractPlayer __instance, DamageInfo info) {
        if (info.output > 0) {
            if (CompanionField.currCompanion.get(__instance) != null && CompanionField.currCompanion.get(__instance).hasPower(ManiacPower.POWER_ID)) {
                CompanionField.currCompanion.get(__instance).getPower(ManiacPower.POWER_ID).onSpecificTrigger();
            }
        }
    }
}
