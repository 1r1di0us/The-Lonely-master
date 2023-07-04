package lonelymod.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import lonelymod.LonelyCharacter;

@SpirePatch(
        clz=GameActionManager.class,
        method="callEndOfTurnActions"
)
public class CompanionTakeTurnPatch {
    public static void Postfix(GameActionManager __instance) {
        if (LonelyCharacter.currCompanion != null) {
            LonelyCharacter.currCompanion.takeTurn();
        }
    }
}
