package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import lonelymod.fields.CompanionField;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "draw",
        paramtypez = {
                int.class
})

public class OnCardDrawPatch {
    @SpireInsertPatch(
            locator=Locator.class,
            localvars={"c"}
    )

    public static void Insert(AbstractPlayer __instance, int numCards, AbstractCard c) {
        if (CompanionField.currCompanion.get(__instance) != null) {
            for (AbstractPower p : CompanionField.currCompanion.get(__instance).powers) {
                p.onCardDraw(c);
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");

            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[0]};
        }
    }
}
