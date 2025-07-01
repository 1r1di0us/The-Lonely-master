package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import lonelymod.fields.CompanionField;

import java.util.ArrayList;

@SpirePatch(
        clz=ChangeStanceAction.class,
        method="update"
)
public class OnChangeStancePatch {
    @SpireInsertPatch(
            locator= OnChangeStancePatch.Locator.class,
            localvars={"oldStance"}
    )

    public static void Insert(ChangeStanceAction __instance, AbstractStance oldStance) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            for (AbstractPower p : CompanionField.currCompanion.get(AbstractDungeon.player).powers) {
                p.onChangeStance(oldStance, AbstractDungeon.player.stance);
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractStance.class, "onEnterStance");

            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[0]};
        }
    }
}
