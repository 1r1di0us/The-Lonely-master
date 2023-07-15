package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import lonelymod.fields.CompanionField;
import lonelymod.powers.BonesPower;

import java.util.ArrayList;

@SpirePatch(
        clz= AbstractMonster.class,
        method="die",
        paramtypez={
                boolean.class
        }
)
public class OnMonsterDeathPatch {

    @SpireInsertPatch(
            locator=Locator.class,
            localvars={}
    )

    public static void Insert(AbstractMonster __instance, boolean triggerRelics) {
        if (triggerRelics && CompanionField.currCompanion.get(AbstractDungeon.player) != null)
            for (AbstractPower p : CompanionField.currCompanion.get(AbstractDungeon.player).powers)
                if (p instanceof BonesPower)
                    ((BonesPower) p).onMonsterDeath(__instance);
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            CompanionField.currCompanion.get(AbstractDungeon.player).getTarget();
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(MonsterGroup.class, "areMonstersBasicallyDead");

            return LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
        }
    }
}
