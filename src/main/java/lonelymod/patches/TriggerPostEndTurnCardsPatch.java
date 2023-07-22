package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import lonelymod.actions.TriggerPostEndTurnCardsAction;

import java.util.ArrayList;

@SpirePatch(
        clz= AbstractRoom.class,
        method="endTurn"
)
public class TriggerPostEndTurnCardsPatch {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {}
    )
    public static void Insert(AbstractRoom __instance) {
        AbstractDungeon.actionManager.addToBottom(new TriggerPostEndTurnCardsAction(AbstractDungeon.player));
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(CardGroup.class, "group");

            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[0]};
        }
    }
}
