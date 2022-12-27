package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import lonelymod.interfaces.OnManualDiscardInterface;

public class OnManualDiscardPatch {

    @SpirePatch(
        clz=AbstractCard.class,
        method="triggerOnManualDiscard"

    )
    public static class triggerPowersOnManualDiscardPatch {

        public static void Prefix(AbstractCard __instance) {
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof OnManualDiscardInterface) {
                    ((OnManualDiscardInterface) p).OnManualDiscard(__instance);
                }
            }
        }
    }
}
