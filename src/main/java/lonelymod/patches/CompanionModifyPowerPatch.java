package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.LonelyCharacter;
import lonelymod.fields.CompanionField;

@SpirePatch(
        clz=AbstractDungeon.class,
        method="onModifyPower"
)
public class CompanionModifyPowerPatch {

    public static void Postfix() {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            CompanionField.currCompanion.get(AbstractDungeon.player).applyPowers();
        }
    }
}
