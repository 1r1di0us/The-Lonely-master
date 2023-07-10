package lonelymod.patches;


import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterQueueItem;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import lonelymod.LonelyCharacter;
import lonelymod.fields.CompanionField;

import java.util.ArrayList;

@SpirePatch(
        clz=GameActionManager.class,
        method="getNextAction"
)
public class CompanionTakeTurnPatch {

    @SpireInsertPatch(
            locator=Locator.class,
            localvars={}
    )

    public static void Insert(GameActionManager __instance) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            if (!(AbstractDungeon.getCurrRoom()).skipMonsterTurn) {
                AbstractDungeon.actionManager.monsterQueue.add(new MonsterQueueItem(CompanionField.currCompanion.get(AbstractDungeon.player)));
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(GameActionManager.class, "monsterAttacksQueued");

            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[1]};
        }
    }
}
