package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import lonelymod.fields.CompanionField;

import java.util.ArrayList;

@SpirePatch(
        clz=MonsterGroup.class,
        method="applyEndOfTurnPowers"
)
public class CompanionEndOfRoundPowersPatch {

    @SpireInsertPatch(
            locator=Locator.class,
            localvars={}
    )

    public static void Insert(MonsterGroup __instance) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null)
            for (AbstractPower p: CompanionField.currCompanion.get(AbstractDungeon.player).powers)
                p.atEndOfRound(); //this means at the end of the round, which is at the start of the turn.
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(MonsterGroup.class, "monsters");

            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[1]};
        }
    }
}
