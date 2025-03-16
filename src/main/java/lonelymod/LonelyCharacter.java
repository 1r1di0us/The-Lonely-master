package lonelymod;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomEnergyOrb;
import basemod.abstracts.CustomUnlockBundle;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import lonelymod.cards.Defend;
import lonelymod.cards.Sic;
import lonelymod.cards.Heel;
import lonelymod.cards.Strike;
import lonelymod.relics.BonesStomach;
import lonelymod.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.ui.panels.AbstractPanel;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static lonelymod.LonelyCharacter.Enums.YELLOW;
import static lonelymod.LonelyMod.*;

import java.util.ArrayList;
import java.util.List;

public class LonelyCharacter extends CustomPlayer {

    static final String ID = makeID("Lonely");
    static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    static final String[] NAMES = characterStrings.NAMES;
    static final String[] TEXT = characterStrings.TEXT;

    private static final Float SIZE_SCALE = 0.8F;
    private static final float ORB_IMG_SCALE = 1.15F * Settings.scale;

    private CustomUnlockBundle unlocks0;
    private CustomUnlockBundle unlocks1;
    private CustomUnlockBundle unlocks2;
    private CustomUnlockBundle unlocks3;
    private CustomUnlockBundle unlocks4;
    //https://github.com/mikemayhemdev/DownfallSTS/blob/master/src/main/java/downfall/downfallMod.java#L1655
    //https://github.com/daviscook477/BaseMod/blob/master/mod/src/main/java/basemod/abstracts/CustomUnlockBundle.java

    public LonelyCharacter(String name, PlayerClass setClass) {
        super(name, setClass, new CustomEnergyOrb(orbTextures, modID + "Resources/images/char/mainChar/orb/vfx.png", new float[] {
                40f, 20f, 15f, -10f, 0f
            }) {
                private FrameBuffer orbFbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
                private Texture orbMask = TexLoader.getTexture(makeImagePath("char/mainChar/orb/mask.png"));

                @Override
                public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
                    sb.end();
                    orbFbo.begin();
                    Gdx.gl.glClearColor(0, 0, 0, 0);
                    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                    Gdx.gl.glColorMask(true, true, true, true);
                    sb.begin();
                    sb.setColor(Color.WHITE);
                    sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                    for(int i = 0; i < energyLayers.length; ++i) {
                        float moveX = 0f, moveY = 0f;
                        if (i == 1) {
                            moveX = 63f;
                            moveY = 52f;
                        } else if (i >= 2) {
                            moveX = -52f;
                            moveY = -44f;
                        }
                        sb.draw((enabled ? energyLayers : noEnergyLayers)[i], current_x - 192f + moveX * ORB_IMG_SCALE, current_y - 192f + moveY * ORB_IMG_SCALE, 192f, 192f, 384.0F, 384.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angles[i], 0, 0, 384, 384, false, false);
                    }
                    sb.setBlendFunction(0, GL20.GL_SRC_ALPHA);
                    sb.setColor(Color.WHITE);
                    sb.draw(orbMask, current_x - 192, current_y - 192, 192, 192, 384, 384, ORB_IMG_SCALE, ORB_IMG_SCALE, 0, 0, 0, 384, 384, false, false);
                    sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                    sb.end();
                    orbFbo.end();
                    sb.begin();
                    TextureRegion drawTex = new TextureRegion(orbFbo.getColorBufferTexture());
                    drawTex.flip(false, true);
                    sb.draw(drawTex, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT);
                    sb.draw(baseLayer, current_x - 192.0F, current_y - 192.0F, 192.0F, 192.0F, 384.0F, 384.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, 0, 0, 0, 384, 384, false, false);
                }
            },
            new SpriterAnimation(
                modID + "Resources/images/char/mainChar/static.scml"));
        initializeClass(null,
                SHOULDER1,
                SHOULDER2,
                CORPSE,
                getLoadout(), 20.0F, -10.0F, 166.0F, 327.0F, new EnergyManager(3)
        );

        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 240.0F * Settings.scale);
    }

    @SpirePatch(clz = EnergyPanel.class, method="renderVfx")
    public static class RenderEnergyVFXPatch {
        public static SpireReturn<Void> Prefix(EnergyPanel __instance, SpriteBatch sb) {
            if (AbstractDungeon.player instanceof LonelyCharacter) {
                if ((float)ReflectionHacks.getPrivate(__instance, EnergyPanel.class, "energyVfxTimer") != 0.0F) {
                    sb.setBlendFunction(770, 1);
                    sb.setColor((Color)ReflectionHacks.getPrivate(__instance, EnergyPanel.class, "energyVfxColor"));
                    sb.draw((Texture)ReflectionHacks.getPrivate(__instance, EnergyPanel.class, "gainEnergyImg"), (float)ReflectionHacks.getPrivate(__instance, AbstractPanel.class, "current_x") - 196.0F, (float)ReflectionHacks.getPrivate(__instance, AbstractPanel.class, "current_y") - 196.0F, 196.0F, 196.0F, 384.0F, 384.0F, (float)ReflectionHacks.getPrivate(__instance, EnergyPanel.class, "energyVfxScale"), (float)ReflectionHacks.getPrivate(__instance, EnergyPanel.class, "energyVfxScale"), -(float)ReflectionHacks.getPrivate(__instance, EnergyPanel.class, "energyVfxAngle") + 50.0F, 0, 0, 384, 384, true, false);
                    sb.draw((Texture)ReflectionHacks.getPrivate(__instance, EnergyPanel.class, "gainEnergyImg"), (float)ReflectionHacks.getPrivate(__instance, AbstractPanel.class, "current_x") - 196.0F, (float)ReflectionHacks.getPrivate(__instance, AbstractPanel.class, "current_y") - 196.0F, 196.0F, 196.0F, 384.0F, 384.0F, (float)ReflectionHacks.getPrivate(__instance, EnergyPanel.class, "energyVfxScale"), (float)ReflectionHacks.getPrivate(__instance, EnergyPanel.class, "energyVfxScale"), (float)ReflectionHacks.getPrivate(__instance, EnergyPanel.class, "energyVfxAngle"), 0, 0, 384, 384, false, false);
                    sb.setBlendFunction(770, 771);
                } 
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                78, 78, 0, 99, 5, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            retVal.add(Strike.ID);
        }
        for (int i = 0; i < 4; i++) {
            retVal.add(Defend.ID);
        }
        retVal.add(Sic.ID);
        retVal.add(Heel.ID);
        return retVal;
    }

    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(BonesStomach.ID);
        return retVal;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("UNLOCK_PING", MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false);
    }

    private static final String[] orbTextures = {
            modID + "Resources/images/char/mainChar/orb/layer1.png",
            modID + "Resources/images/char/mainChar/orb/layer2.png",
            modID + "Resources/images/char/mainChar/orb/layer3.png",
            modID + "Resources/images/char/mainChar/orb/layer4.png",
            modID + "Resources/images/char/mainChar/orb/layer5.png",
            modID + "Resources/images/char/mainChar/orb/layer6.png",
            modID + "Resources/images/char/mainChar/orb/layer1d.png",
            modID + "Resources/images/char/mainChar/orb/layer2d.png",
            modID + "Resources/images/char/mainChar/orb/layer3d.png",
            modID + "Resources/images/char/mainChar/orb/layer4d.png",
            modID + "Resources/images/char/mainChar/orb/layer5d.png",};

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "UNLOCK_PING";
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 8;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return YELLOW;
    }

    @Override
    public Color getCardTrailColor() {
        return characterColor.cpy();
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        AbstractCard GremlinCard = new Sic();
        return GremlinCard;
    }

    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new LonelyCharacter(name, chosenClass);
    }

    @Override
    public Color getCardRenderColor() {
        return characterColor.cpy();
    }

    @Override
    public Color getSlashAttackColor() {
        return characterColor.cpy();
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                AbstractGameAction.AttackEffect.BLUNT_LIGHT,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY};
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    @Override
    public String getSensoryStoneText() { return TEXT[3]; }

    //@Override
    //public Texture getCutsceneBg() {
    //    return ImageMaster.loadImage(modID + "Resources/images/scenes/bkg.png");// 307
    //}

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        List<CutscenePanel> panels = new ArrayList();// 312
        panels.add(new CutscenePanel(modID + "Resources/images/scenes/ending_1.png"));// 313
        panels.add(new CutscenePanel(modID + "Resources/images/scenes/ending_2.png", "ATTACK_HEAVY"));// 314
        panels.add(new CutscenePanel(modID + "Resources/images/scenes/ending_3.png"));// 315
        return panels;// 316
    }

    //animation for replacing the starter relic
    public void onEquipMeatsStomach() {
        /*loadAnimation("lonelymodResources/images/char/mainChar/NewProject2.atlas", "lonelymodResources/images/char/mainChar/NewProject2.json", SIZE_SCALE);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        this.stateData.setMix("Hit", "Idle", 0.1F);
        e.setTimeScale(0.7F);*/
    }

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_LONELY;
        @SpireEnum(name = "yellow")
        public static AbstractCard.CardColor YELLOW;
        @SpireEnum(name = "yellow")
        @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }
}
