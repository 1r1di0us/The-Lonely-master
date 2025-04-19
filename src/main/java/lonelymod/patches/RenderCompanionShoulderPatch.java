package lonelymod.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import lonelymod.companions.Bones;
import lonelymod.companions.Meat;
import lonelymod.relics.BonesStomach;
import lonelymod.relics.MeatsStomach;

@SpirePatch(
        clz= AbstractPlayer.class,
        method="renderShoulderImg",
        paramtypez = {
                SpriteBatch.class
        }
)
public class RenderCompanionShoulderPatch {

    public static void Postfix(AbstractPlayer __instance, SpriteBatch sb) {
        if (__instance.hasRelic(BonesStomach.ID)) {
            if (CampfireUI.hidden) {
                sb.draw(Bones.shoulderImg, 0.0F, 0.0F, 1920.0F * Settings.scale, 1136.0F * Settings.scale);
            } else {
                sb.draw(Bones.shoulder2Img, __instance.animX, 0.0F, 1920.0F * Settings.scale, 1136.0F * Settings.scale);
            }
        }
        else if (__instance.hasRelic(MeatsStomach.ID)) {
            if (CampfireUI.hidden) {
                sb.draw(Meat.shoulderImg, 0.0F, 0.0F, 1920.0F * Settings.scale, 1136.0F * Settings.scale);
            } else {
                sb.draw(Meat.shoulder2Img, __instance.animX, 0.0F, 1920.0F * Settings.scale, 1136.0F * Settings.scale);
            }
        }
    }

}
