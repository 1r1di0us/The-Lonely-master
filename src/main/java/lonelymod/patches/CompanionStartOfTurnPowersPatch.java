package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import lonelymod.fields.CompanionField;

import java.util.ArrayList;

@SpirePatch(
        clz= GameActionManager.class,
        method="getNextAction"
)
public class CompanionStartOfTurnPowersPatch {
    @SpireInsertPatch(
            locator= CompanionStartOfTurnPowersPatch.Locator.class,
            localvars={}
    )

    public static void Insert(GameActionManager __instance) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            CompanionField.currCompanion.get(AbstractDungeon.player).applyStartOfTurnPowers();
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "applyStartOfTurnOrbs");

            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[0]};
        }
    }
}
