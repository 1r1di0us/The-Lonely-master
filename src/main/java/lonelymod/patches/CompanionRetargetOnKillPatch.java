package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.fields.CompanionField;
import lonelymod.powers.TargetPower;

@SpirePatch(
        clz=AbstractMonster.class,
        method="die",
        paramtypez = {
                boolean.class
        }
)
public class CompanionRetargetOnKillPatch {

    public static void Postfix(AbstractMonster __instance, boolean triggerRelics) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null && AbstractDungeon.overlayMenu.endTurnButton.enabled) {
            CompanionField.currCompanion.get(AbstractDungeon.player).getTarget();
            CompanionField.currCompanion.get(AbstractDungeon.player).applyPowers();
        }
    }
}
