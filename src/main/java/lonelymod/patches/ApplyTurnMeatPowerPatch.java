package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.companions.Meat;
import lonelymod.fields.CompanionField;
import lonelymod.powers.MeatPower;

import static lonelymod.LonelyMod.makeID;

@SpirePatch(
        clz= AbstractCreature.class,
        method="applyTurnPowers"
)
public class ApplyTurnMeatPowerPatch {

    public static void Postfix(AbstractCreature __instance) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) instanceof Meat) {
            if (CompanionField.currCompanion.get(AbstractDungeon.player).hasPower(makeID("MeatPower"))) {
                ((MeatPower) CompanionField.currCompanion.get(AbstractDungeon.player).getPower(makeID("MeatPower"))).creature = __instance;
                CompanionField.currCompanion.get(AbstractDungeon.player).getPower(makeID("MeatPower")).onSpecificTrigger();
            }
        }
    }
}
