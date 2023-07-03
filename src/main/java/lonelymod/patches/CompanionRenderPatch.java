package lonelymod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import lonelymod.LonelyCharacter;

@SpirePatch(
        clz= AbstractPlayer.class,
        method="render"
)
public class CompanionRenderPatch {
    public static void Prefix(AbstractPlayer __instance, SpriteBatch sb) {
        if (LonelyCharacter.currCompanion != null) {
            LonelyCharacter.currCompanion.render(sb);
        }
    }
}
