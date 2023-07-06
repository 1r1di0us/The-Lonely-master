package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import lonelymod.LonelyCharacter;

import java.util.ArrayList;

@SpirePatch(
        clz=MonsterGroup.class,
        method="applyEndOfTurnPowers"
)
public class CompanionEndOfTurnPowersPatch {

    @SpireInsertPatch(
            locator=Locator.class,
            localvars={}
    )

    public static void Insert(MonsterGroup __instance) {
        if (LonelyCharacter.currCompanion != null)
            for (AbstractPower p: LonelyCharacter.currCompanion.powers)
                p.atEndOfRound();
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractMonster.class, "monsters");

            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[1]};
        }
    }
}
