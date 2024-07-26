package lonelymod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import lonelymod.fields.CompanionField;

@SpirePatch(
        clz= AbstractPlayer.class,
        method="render"
)
public class CompanionTipRenderPatch {
    public static void Postfix(AbstractPlayer __instance, SpriteBatch sb) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            if (CompanionField.hoveredCompanion.get(AbstractDungeon.player) != null && AbstractDungeon.player.hoverEnemyWaitTimer < 0.0F)
                if (!AbstractDungeon.isScreenUp || PeekButton.isPeeking)
                    CompanionField.hoveredCompanion.get(AbstractDungeon.player).renderTip(sb);
        }
    }
}
