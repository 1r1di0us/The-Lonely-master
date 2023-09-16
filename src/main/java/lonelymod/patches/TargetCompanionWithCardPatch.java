package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import lonelymod.fields.CompanionField;
import lonelymod.powers.LonelyPower;

import java.util.ArrayList;
import java.util.Objects;

public class TargetCompanionWithCardPatch {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method="updateSingleTargetInput"
    )
    public static class updateSingleTargetInputPatch {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars={"hoveredCard", "hoveredMonster"}
        )
        public static void Insert(AbstractPlayer __instance, AbstractCard hoveredCard, @ByRef AbstractMonster[] hoveredMonster) {
            if (CompanionField.currCompanion.get(__instance) != null && CompanionField.playableCards.get(__instance) != null) {
                for (AbstractCard c : CompanionField.playableCards.get(__instance)) {
                    if (Objects.equals(hoveredCard.cardID, c.cardID)) {
                        CompanionField.currCompanion.get(__instance).hb.update();
                        if (CompanionField.currCompanion.get(__instance).hb.hovered) {
                            hoveredMonster[0] = CompanionField.currCompanion.get(__instance);
                        }
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCurrRoom");

                return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[0]};
            }
        }
    }

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
            if (CompanionField.playableCards.get(AbstractDungeon.player) != null) {
                for (AbstractCard c : CompanionField.playableCards.get(__instance)) {
                    if (Objects.equals(hoveredCard.cardID, c.cardID) && CompanionField.currCompanion.get(__instance) != null) {
                        sortedMonsters.add(CompanionField.currCompanion.get(__instance));
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "sort");

                return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher)[0]};
            }
        }
    }

    @SpirePatch(
            clz=AbstractPlayer.class,
            method="useCard",
            paramtypez = {
                    AbstractCard.class,
                    AbstractMonster.class,
                    int.class
    }
    )
    public static class UseCardPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractCard.class.getName()) && m.getMethodName().equals("use")) {
                        m.replace("if (lonelymod.patches.TargetCompanionWithCardPatch.useTheCard($0, $1, $2)) $proceed($$);");
                    }
                }
            };
        }
    }

    public static boolean useTheCard(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        boolean proceed = true;
        for (AbstractPower pow : p.powers) { //LonelyPower overrides this
            if (pow instanceof LonelyPower && pow.amount == ((LonelyPower) pow).maxAmount - 1) {
                proceed = false;
                break;
            }
        }
        if (proceed) {
            if (CompanionField.currCompanion.get(p) != null && CompanionField.playableCards.get(p) != null && CompanionField.currCompanion.get(p) == m) {
                for (AbstractCard c : CompanionField.playableCards.get(p)) {
                    if (Objects.equals(c.cardID, (card).cardID)) {
                        CompanionField.currCompanion.get(p).useTheCard(card, p, m);
                        proceed = false;
                        break;
                    }
                }
            }
        }
        return proceed;
    }
}
