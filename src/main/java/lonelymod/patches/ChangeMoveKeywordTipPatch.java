package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;

import java.util.TreeMap;

public class ChangeMoveKeywordTipPatch {
    @SpirePatch(
            clz = TipHelper.class,
            method = "renderKeywords"
    )
    public static class TipHelperRenderKeywordsPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(TreeMap.class.getName()) && m.getMethodName().equals("get")) {
                        m.replace("$_ = lonelymod.patches.ChangeMoveKeywordTipPatch.getKeywordString($$);");
                    }
                }
            };
        }
    }

    @SpirePatch(
            clz = TipHelper.class,
            method = "renderBox"
    )
    public static class TipHelperRenderBoxPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(TreeMap.class.getName()) && m.getMethodName().equals("get")) {
                        m.replace("$_ = lonelymod.patches.ChangeMoveKeywordTipPatch.getKeywordString($$);");
                    }
                }
            };
        }
    }

    @SpirePatch(
        clz = TipHelper.class,
        method="renderBox"
    )
    public static class TipHelperRenderBoxHeaderPatch {
        private static int i = 0;
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(TipHelper.class.getName()) && m.getMethodName().equals("capitalize")) {
                        if (i == 1) {
                            m.replace("$_ = lonelymod.patches.ChangeMoveKeywordTipPatch.getKeywordHeaderString($$);");
                            i++;
                        } else if (i == 0) {
                            i++;
                        }
                    }
                }
            };
        }
    }

    public static String getKeywordString(Object s) {
        String body = GameDictionary.keywords.get(s);
        String replace;
        if (CardCrawlGame.isInARun() && CompanionField.currCompanion.get(AbstractDungeon.player) != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && AbstractDungeon.player.hoveredCard != null && AbstractDungeon.player.hoveredCard.isHoveredInHand(1.0F)) {
            if (body.contains("Move: Focuses on dealing damage to enemies.")) {
                replace = CompanionField.currCompanion.get(AbstractDungeon.player).getKeywordMoveTip(AbstractCompanion.ATTACK, false);
            } else if (body.contains("Move: Focuses on granting you #yBlock or weakening enemies.")) {
                replace = CompanionField.currCompanion.get(AbstractDungeon.player).getKeywordMoveTip(AbstractCompanion.PROTECT, false);
            } else if (body.contains("Move: The most powerful #yMove. NL Effects vary greatly between #yCompanions.")) {
                replace = CompanionField.currCompanion.get(AbstractDungeon.player).getKeywordMoveTip(AbstractCompanion.SPECIAL, false);
            } else {
                replace = body;
            }
        } else {
            replace = body;
        }
        return replace;
    }

    public static String getKeywordHeaderString(Object s) {
        String header = TipHelper.capitalize((String) s);
        String replace;
        if (CardCrawlGame.isInARun() && CompanionField.currCompanion.get(AbstractDungeon.player) != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && AbstractDungeon.player.hoveredCard != null && AbstractDungeon.player.hoveredCard.isHoveredInHand(1.0F)) {
            if (header.equals("Attack")) {
                replace = CompanionField.currCompanion.get(AbstractDungeon.player).getKeywordMoveTip(AbstractCompanion.ATTACK, true);
            } else if (header.equals("Protect")) {
                replace = CompanionField.currCompanion.get(AbstractDungeon.player).getKeywordMoveTip(AbstractCompanion.PROTECT, true);
            } else if (header.equals("Special")) {
                replace = CompanionField.currCompanion.get(AbstractDungeon.player).getKeywordMoveTip(AbstractCompanion.SPECIAL, true);
            } else {
                replace = header;
            }
        } else {
            replace = header;
        }
        return replace;
    }
}
