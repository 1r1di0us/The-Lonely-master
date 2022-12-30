package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;

import lonelymod.interfaces.TriggerOnHandSizeInterface;

public class RefreshHandLayoutHookPatch {
    
    @SpirePatch(
        clz=CardGroup.class,
        method="refreshHandLayout"
    )
    public static class triggerOnHandSizePatch {
        
        @SpirePostfixPatch
        public static void triggerOnHandSize(CardGroup __instance) {
            if (__instance.type == CardGroupType.HAND) {
                for (AbstractCard c : __instance.group) {
                    if (c instanceof TriggerOnHandSizeInterface) {
                        ((TriggerOnHandSizeInterface) c).triggerOnHandSize();
                    }
                }
            }
        }
    }
}
