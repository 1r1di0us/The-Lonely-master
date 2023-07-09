package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.LonelyCharacter;

@SpirePatch(
        clz=AbstractDungeon.class,
        method="onModifyPower"
)
public class CompanionModifyPowerPatch {

    public static void Postfix() {
        if (LonelyCharacter.currCompanion != null) {
            LonelyCharacter.currCompanion.applyPowers();
        }
    }
}
