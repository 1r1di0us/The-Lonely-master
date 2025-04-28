package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.DeathScreen;
import lonelymod.fields.CompanionField;

@SpirePatch(
        clz= DeathScreen.class,
        method=SpirePatch.CONSTRUCTOR,
        paramtypez = {
                MonsterGroup.class
        }
)

public class EscapeOnPlayerDeathPatch {

    public static void Prefix(DeathScreen __instance, MonsterGroup m) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            CompanionField.currCompanion.get(AbstractDungeon.player).escape();
        }
    }
}
