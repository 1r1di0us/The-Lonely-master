package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import lonelymod.fields.CompanionField;

import java.util.ArrayList;

@SpirePatch(
        clz=AbstractPlayer.class,
        method="damage"
)
public class OnPlayerLoseHpPatch {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = "damageAmount"
    )
    public static void Insert(AbstractPlayer __instance, DamageInfo info, int damageAmount) {
        if (CompanionField.currCompanion.get(__instance) != null) {
            for (AbstractPower p : CompanionField.currCompanion.get(__instance).powers) {
                p.wasHPLost(info, damageAmount);
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");

            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[6]};
        }
    }
}
