package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import lonelymod.companions.AbstractCompanion;

import java.util.ArrayList;

@SpirePatch(
        clz= AbstractDungeon.class,
        method="nextRoomTransition",
        paramtypez={
                SaveFile.class
        }
)

public class CompanionRngTransitionPatch {
    @SpireInsertPatch(
            locator= CompanionRngTransitionPatch.Locator.class,
            localvars={"saveFile"}
    )

    public static void Insert(AbstractDungeon __instance, SaveFile saveFile) {
        AbstractCompanion.companionRng = new Random(Settings.seed + (long)AbstractDungeon.floorNum);
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(Settings.class, "seed");

            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[4]};
        }
    }

}
