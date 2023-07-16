package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;

import java.util.TreeMap;

public class ChangeMoveKeywordTipPatch {
    private static final String keyKey = "[hydrologistmod:swap_key]";

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
        if (body.contains("A #yMove that focuses on dealing damage to a random enemy.")) {
            replace = CompanionField.currCompanion.get(AbstractDungeon.player).getKeywordMoveTip(AbstractCompanion.ATTACK, false);
        } else if (body.contains("A #yMove focusing on granting you #yBlock, or weakening a random enemy.")) {
            replace = CompanionField.currCompanion.get(AbstractDungeon.player).getKeywordMoveTip(AbstractCompanion.PROTECT, false);
        } else if (body.contains("The most powerful #yMove. NL Effects vary greatly between #yCompanions.")) {
            replace = CompanionField.currCompanion.get(AbstractDungeon.player).getKeywordMoveTip(AbstractCompanion.SPECIAL, false);
        } else {
            replace = body;
        }
        return replace;
    }

    public static String getKeywordHeaderString(Object s) {
        String header = TipHelper.capitalize((String) s);
        String replace;
        if (header.contains("ATTACK MOVE")) {
            replace = CompanionField.currCompanion.get(AbstractDungeon.player).getKeywordMoveTip(AbstractCompanion.ATTACK, true);
        } else if (header.contains("PROTECT MOVE")) {
            replace = CompanionField.currCompanion.get(AbstractDungeon.player).getKeywordMoveTip(AbstractCompanion.PROTECT, true);
        } else if (header.contains("SPECIAL MOVE")) {
            replace = CompanionField.currCompanion.get(AbstractDungeon.player).getKeywordMoveTip(AbstractCompanion.SPECIAL, true);
        } else {
            replace = header;
        }
        return replace;
    }
}
