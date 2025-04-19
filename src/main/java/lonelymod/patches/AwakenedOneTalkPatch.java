package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import lonelymod.LonelyCharacter;

import static lonelymod.LonelyMod.makeID;

@SpirePatch(
        clz = AwakenedOne.class,
        method = "usePreBattleAction"
)

public class AwakenedOneTalkPatch {

    private static final UIStrings uistring = CardCrawlGame.languagePack.getUIString(makeID("AwakenedOneMessage"));
    public static final String[] TEXT = uistring.TEXT;

    public static void Postfix(AwakenedOne __instance) {
        if (AbstractDungeon.player.chosenClass == LonelyCharacter.Enums.THE_LONELY) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(__instance, TEXT[0], 0.5F, 2.0F));
        }
    }
}
