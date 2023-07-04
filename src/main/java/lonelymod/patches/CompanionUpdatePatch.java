package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import lonelymod.LonelyCharacter;

@SpirePatch(
        clz= MonsterGroup.class,
        method="update"
)
public class CompanionUpdatePatch {

    public static void Prefix(MonsterGroup __instance) {
        if (LonelyCharacter.currCompanion != null) {
            LonelyCharacter.currCompanion.update();
        }
    }

}
