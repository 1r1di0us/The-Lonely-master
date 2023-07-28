package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import lonelymod.actions.CompanionTakeTurnAction;
import lonelymod.fields.CompanionField;
import lonelymod.powers.WildFormPower;

import java.util.ArrayList;

@SpirePatch(
        clz=AbstractRoom.class,
        method="endTurn"
)
public class CompanionTakeTurnPatch {
    @SpireInsertPatch(
            locator=Locator.class,
            localvars={}
    )

    public static void Insert(AbstractRoom __instance) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            if (!__instance.skipMonsterTurn) {
                AbstractDungeon.actionManager.addToBottom(new CompanionTakeTurnAction(true));
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "actionManager");

            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[2]};
        }
    }
}
