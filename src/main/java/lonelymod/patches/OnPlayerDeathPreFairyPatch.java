package lonelymod.patches;

import lonelymod.interfaces.OnPlayerDeathPreFairyInterface;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "damage",
        paramtypez = {DamageInfo.class}
)
public class OnPlayerDeathPreFairyPatch {
    @SpireInsertPatch(
            locator = OnPlayerDeathPreFairyPatch.Locator.class
    )
    public static SpireReturn Insert(AbstractPlayer __instance, DamageInfo info) {
        for (AbstractPower power : __instance.powers) {
            if (power instanceof OnPlayerDeathPreFairyInterface && !((OnPlayerDeathPreFairyInterface) power).onPlayerDeathPreFairy(__instance, info)) {
                return SpireReturn.Return((Object) null);
            }
        }
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
            return new int[] {LineFinder.findInOrder(ctBehavior, finalMatcher)[0]};
        }
    }
}