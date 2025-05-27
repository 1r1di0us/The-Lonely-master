package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.EnableEndTurnButtonAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import lonelymod.fields.CompanionField;

import java.util.ArrayList;

@SpirePatch(
        clz= GameActionManager.class,
        method="getNextAction"
)
public class CompanionStartOfTurnPostDrawPowersPatch {
    @SpireInsertPatch(
            locator = CompanionStartOfTurnPostDrawPowersPatch.Locator.class,
            localvars = {}
    )

    public static void Insert(GameActionManager __instance) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            CompanionField.currCompanion.get(AbstractDungeon.player).applyStartOfTurnPostDrawPowers();
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.NewExprMatcher(EnableEndTurnButtonAction.class);

            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[0]};
        }
    }
}
