package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import lonelymod.LonelyCharacter;
import lonelymod.fields.CompanionField;

@SpirePatch(
        clz= MonsterGroup.class,
        method="update"
)
public class CompanionUpdatePatch {

    public static void Prefix(MonsterGroup __instance) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            CompanionField.currCompanion.get(AbstractDungeon.player).update();
        }
    }

}
