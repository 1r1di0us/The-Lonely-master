package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import lonelymod.LonelyCharacter;

@SpirePatch(
        clz=MonsterGroup.class,
        method="showIntent"
)
public class CompanionShowIntentPatch {

    public static void Postfix(MonsterGroup __instance) {
        if (LonelyCharacter.currCompanion != null) {
            LonelyCharacter.currCompanion.createIntent();
        }
    }
}
