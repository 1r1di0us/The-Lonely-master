package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FocusPower;
import lonelymod.fields.CompanionField;
import lonelymod.powers.MechanicPower;


public class GainLoseFocusPatch {

    @SpirePatch(clz = FocusPower.class, method = "onInitialApplication")
    public static class ApplyFocusPatch {
        public static void Prefix(FocusPower __instance) {
            if (CompanionField.currCompanion.get(AbstractDungeon.player) != null && CompanionField.currCompanion.get(AbstractDungeon.player).hasPower(MechanicPower.POWER_ID)) {
                CompanionField.currCompanion.get(AbstractDungeon.player).getPower(MechanicPower.POWER_ID).onSpecificTrigger();
            }
        }
    }

    @SpirePatch(clz=FocusPower.class, method="stackPower")
    public static class StackFocusPatch {
        public static void Postfix(FocusPower __instance, int stackAmount) {
            if (CompanionField.currCompanion.get(AbstractDungeon.player) != null && CompanionField.currCompanion.get(AbstractDungeon.player).hasPower(MechanicPower.POWER_ID)) {
                CompanionField.currCompanion.get(AbstractDungeon.player).getPower(MechanicPower.POWER_ID).onSpecificTrigger();
            }
        }
    }

    @SpirePatch(clz= FocusPower.class, method="reducePower")
    public static class ReduceFocusPatch {
        public static void Postfix(FocusPower __instance, int reduceAmount) {
            if (CompanionField.currCompanion.get(AbstractDungeon.player) != null && CompanionField.currCompanion.get(AbstractDungeon.player).hasPower(MechanicPower.POWER_ID)) {
                CompanionField.currCompanion.get(AbstractDungeon.player).getPower(MechanicPower.POWER_ID).onSpecificTrigger();
            }
        }
    }
}