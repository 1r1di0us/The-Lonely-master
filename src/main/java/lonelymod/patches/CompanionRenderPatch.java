package lonelymod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import lonelymod.LonelyCharacter;

@SpirePatch(
        clz= AbstractPlayer.class,
        method="render"
)
public class CompanionRenderPatch {
    public static void Prefix(AbstractPlayer __instance, SpriteBatch sb) {
        if (LonelyCharacter.currCompanion != null) {
            if (LonelyCharacter.hoveredCompanion != null && AbstractDungeon.player.hoverEnemyWaitTimer < 0.0F)
                if (!AbstractDungeon.isScreenUp || PeekButton.isPeeking)
                    LonelyCharacter.hoveredCompanion.renderTip(sb);
            LonelyCharacter.currCompanion.render(sb);
        }
    }
}
