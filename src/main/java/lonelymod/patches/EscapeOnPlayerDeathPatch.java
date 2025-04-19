package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import lonelymod.fields.CompanionField;

import java.util.ArrayList;

@SpirePatch(
        clz= AbstractPlayer.class,
        method="damage",
        paramtypez = {
                DamageInfo.class
        }
)

public class EscapeOnPlayerDeathPatch {

    @SpireInsertPatch(
            locator= EscapeOnPlayerDeathPatch.Locator.class
    )

    public static void Insert(AbstractPlayer __instance, DamageInfo info) {
        if (CompanionField.currCompanion.get(__instance) != null) {
            CompanionField.currCompanion.get(__instance).escape();
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "isDead");

            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[0]};
        }
    }
}
