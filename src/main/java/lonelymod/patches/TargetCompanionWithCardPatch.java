package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import lonelymod.fields.CompanionField;
import lonelymod.powers.SpyPower;

import java.util.ArrayList;
import java.util.Objects;

public class TargetCompanionWithCardPatch {
    @SpirePatch(
            clz= AbstractPlayer.class,
            method="updateTargetArrowWithKeyboard"
    )
    public static class UpdateTargetArrowPatch {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars={"hoveredCard", "sortedMonsters"}
        )
        public static void Insert(AbstractPlayer __instance, boolean autoTargetFirst, AbstractCard hoveredCard, ArrayList<AbstractMonster> sortedMonsters) {
            if (Objects.equals(hoveredCard.cardID, Shiv.ID) &&
                    CompanionField.currCompanion.get(AbstractDungeon.player) != null &&
                    CompanionField.currCompanion.get(AbstractDungeon.player).hasPower(SpyPower.POWER_ID)) {
                sortedMonsters.add(CompanionField.currCompanion.get(AbstractDungeon.player));
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "sort");

                return LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }
}
