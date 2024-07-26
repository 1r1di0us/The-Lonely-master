package lonelymod.tutorials;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;

import static lonelymod.LonelyMod.*;

public class CompanionTutorial extends FtueTip {
    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString(makeID("CompanionTutorial"));
    public static final String[] MSG = tutorialStrings.TEXT;
    public static final String[] LABEL = tutorialStrings.LABEL;

    private static final TextureRegion img1 = new TextureRegion(new Texture(makeImagePath("tutorials/tut1.jpg")));
    private static final TextureRegion img2 = new TextureRegion(new Texture(makeImagePath("tutorials/tut2.jpg")));
    private static final TextureRegion img3 = new TextureRegion(new Texture(makeImagePath("tutorials/tut3.jpg")));
    private static final TextureRegion img4 = new TextureRegion(new Texture(makeImagePath("tutorials/tut4.jpg")));
    private static final TextureRegion img5 = new TextureRegion(new Texture(makeImagePath("tutorials/tut5.jpg")));
    private static final TextureRegion img6 = new TextureRegion(new Texture(makeImagePath("tutorials/tut6.jpg")));

    private Color screen = Color.valueOf("1c262a00");
    private float x, x1, x2, x3, x4, x5, x6, targetX, startX;
    private float scrollTimer = 0f;
    private static final float SCROLL_TIME = 0.3f;
    private int currentSlot = 0;
    private static final String msg1 = MSG[0];
    private static final String msg2 = MSG[1];
    private static final String msg3 = MSG[2];
    private static final String msg4 = MSG[3];
    private static final String msg5 = MSG[4];
    private static final String msg6 = MSG[5];

    public CompanionTutorial() {
        AbstractDungeon.player.releaseCard();
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.FTUE;
        AbstractDungeon.overlayMenu.showBlackScreen();
        x = 0f;
        x1 = 567f * Settings.scale;
        x2 = x1 + Settings.WIDTH;
        x3 = x2 + Settings.WIDTH;
        x4 = x3 + Settings.WIDTH;
        x5 = x4 + Settings.WIDTH;
        x6 = x5 + Settings.WIDTH;
        AbstractDungeon.overlayMenu.proceedButton.show();
        AbstractDungeon.overlayMenu.proceedButton.setLabel(LABEL[0]);
    }

    @Override
    public void update() {
        if (screen.a != 0.8f) {
            screen.a += Gdx.graphics.getDeltaTime();
            if (screen.a > 0.8f) {
                screen.a = 0.8f;
            }
        }

        if (AbstractDungeon.overlayMenu.proceedButton.isHovered && InputHelper.justClickedLeft
                || CInputActionSet.proceed.isJustPressed()) {
            CInputActionSet.proceed.unpress();
            if (currentSlot == -5) {
                CardCrawlGame.sound.play("DECK_CLOSE");
                AbstractDungeon.closeCurrentScreen();
                AbstractDungeon.overlayMenu.proceedButton.hide();
                AbstractDungeon.effectList.clear();
                AbstractDungeon.topLevelEffects.add(new BattleStartEffect(false));
                return;
            }
            AbstractDungeon.overlayMenu.proceedButton.hideInstantly();
            AbstractDungeon.overlayMenu.proceedButton.show();
            CardCrawlGame.sound.play("DECK_CLOSE");
            currentSlot--;
            startX = x;
            targetX = currentSlot * Settings.WIDTH;
            scrollTimer = SCROLL_TIME;

            if (currentSlot == -5) {
                AbstractDungeon.overlayMenu.proceedButton.setLabel(LABEL[1]);
            }
        }

        if (scrollTimer != 0f) {
            scrollTimer -= Gdx.graphics.getDeltaTime();
            if (scrollTimer < 0f) {
                scrollTimer = 0f;
            }
        }

        x = Interpolation.fade.apply(targetX, startX, scrollTimer / SCROLL_TIME);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(screen);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0f, 0f, Settings.WIDTH, Settings.HEIGHT);

        sb.setColor(Color.WHITE);
        sb.draw(img1, x + x1 - img1.getRegionWidth() / 2f, Settings.HEIGHT / 2f - img1.getRegionHeight() / 2f, img1.getRegionWidth() / 2f, img1.getRegionHeight() / 2f, img1.getRegionWidth(), img1.getRegionHeight(), Settings.scale, Settings.scale, 0);
        sb.draw(img2, x + x2 - img2.getRegionWidth() / 2f, Settings.HEIGHT / 2f - img2.getRegionHeight() / 2f, img2.getRegionWidth() / 2f, img2.getRegionHeight() / 2f, img2.getRegionWidth(), img2.getRegionHeight(), Settings.scale, Settings.scale, 0);
        sb.draw(img3, x + x3 - img3.getRegionWidth() / 2f, Settings.HEIGHT / 2f - img3.getRegionHeight() / 2f, img3.getRegionWidth() / 2f, img3.getRegionHeight() / 2f, img3.getRegionWidth(), img3.getRegionHeight(), Settings.scale, Settings.scale, 0);
        sb.draw(img4, x + x4 - img4.getRegionWidth() / 2f, Settings.HEIGHT / 2f - img4.getRegionHeight() / 2f, img4.getRegionWidth() / 2f, img4.getRegionHeight() / 2f, img4.getRegionWidth(), img4.getRegionHeight(), Settings.scale, Settings.scale, 0);
        sb.draw(img5, x + x5 - img5.getRegionWidth() / 2f, Settings.HEIGHT / 2f - img5.getRegionHeight() / 2f, img5.getRegionWidth() / 2f, img5.getRegionHeight() / 2f, img5.getRegionWidth(), img5.getRegionHeight(), Settings.scale, Settings.scale, 0);
        sb.draw(img6, x + x6 - img6.getRegionWidth() / 2f, Settings.HEIGHT / 2f - img6.getRegionHeight() / 2f, img6.getRegionWidth() / 2f, img6.getRegionHeight() / 2f, img6.getRegionWidth(), img6.getRegionHeight(), Settings.scale, Settings.scale, 0);

        float offsetY = 0f;
        if (Settings.BIG_TEXT_MODE) {
            offsetY = 110f * Settings.scale;
        }

        // Message 1
        FontHelper.renderSmartText(sb, FontHelper.panelNameFont, msg1, x + x1 + 400f * Settings.scale, Settings.HEIGHT / 2f - FontHelper.getSmartHeight(FontHelper.panelNameFont, msg1, 700f * Settings.scale, 40f * Settings.scale) / 2f + offsetY, 700f * Settings.scale, 40f * Settings.scale, Settings.CREAM_COLOR);
        FontHelper.renderFontCenteredWidth(sb, FontHelper.panelNameFont, LABEL[4], Settings.WIDTH / 2f, Settings.HEIGHT / 2f - 360f * Settings.scale, Settings.GOLD_COLOR);

        // Message 2
        FontHelper.renderSmartText(sb, FontHelper.panelNameFont, msg2, x + x2 + 400f * Settings.scale, Settings.HEIGHT / 2f - FontHelper.getSmartHeight(FontHelper.panelNameFont, msg2, 700f * Settings.scale, 40f * Settings.scale) / 2f + offsetY, 700f * Settings.scale, 40f * Settings.scale, Settings.CREAM_COLOR);
        //FontHelper.renderFontCenteredWidth(sb, FontHelper.panelNameFont, LABEL[5], Settings.WIDTH / 2f, Settings.HEIGHT / 2f - 360f * Settings.scale, Settings.GOLD_COLOR);

        // Message 3
        FontHelper.renderSmartText(sb, FontHelper.panelNameFont, msg3, x + x3 + 400f * Settings.scale, Settings.HEIGHT / 2f - FontHelper.getSmartHeight(FontHelper.panelNameFont, msg3, 700f * Settings.scale, 40f * Settings.scale) / 2f + offsetY, 700f * Settings.scale, 40f * Settings.scale, Settings.CREAM_COLOR);
        //FontHelper.renderFontCenteredWidth(sb, FontHelper.panelNameFont, LABEL[6], Settings.WIDTH / 2f, Settings.HEIGHT / 2f - 360f * Settings.scale, Settings.GOLD_COLOR);

        // Message 4
        FontHelper.renderSmartText(sb, FontHelper.panelNameFont, msg4, x + x4 + 400f * Settings.scale, Settings.HEIGHT / 2f - FontHelper.getSmartHeight(FontHelper.panelNameFont, msg4, 700f * Settings.scale, 40f * Settings.scale) / 2f + offsetY, 700f * Settings.scale, 40f * Settings.scale, Settings.CREAM_COLOR);
        //FontHelper.renderFontCenteredWidth(sb, FontHelper.panelNameFont, LABEL[7], Settings.WIDTH / 2f, Settings.HEIGHT / 2f - 360f * Settings.scale, Settings.GOLD_COLOR);

        // Message 5
        FontHelper.renderSmartText(sb, FontHelper.panelNameFont, msg5, x + x5 + 400f * Settings.scale, Settings.HEIGHT / 2f - FontHelper.getSmartHeight(FontHelper.panelNameFont, msg5, 700f * Settings.scale, 40f * Settings.scale) / 2f + offsetY, 700f * Settings.scale, 40f * Settings.scale, Settings.CREAM_COLOR);
        //FontHelper.renderFontCenteredWidth(sb, FontHelper.panelNameFont, LABEL[8], Settings.WIDTH / 2f, Settings.HEIGHT / 2f - 360f * Settings.scale, Settings.GOLD_COLOR);

        // Message 6
        FontHelper.renderSmartText(sb, FontHelper.panelNameFont, msg6, x + x6 + 400f * Settings.scale, Settings.HEIGHT / 2f - FontHelper.getSmartHeight(FontHelper.panelNameFont, msg6, 700f * Settings.scale, 40f * Settings.scale) / 2f + offsetY, 700f * Settings.scale, 40f * Settings.scale, Settings.CREAM_COLOR);
        //FontHelper.renderFontCenteredWidth(sb, FontHelper.panelNameFont, LABEL[9], Settings.WIDTH / 2f, Settings.HEIGHT / 2f - 360f * Settings.scale, Settings.GOLD_COLOR);


        FontHelper.renderFontCenteredWidth(sb, FontHelper.tipBodyFont, LABEL[2] + Integer.toString(Math.abs(currentSlot - 1)) + LABEL[3], Settings.WIDTH / 2f, Settings.HEIGHT / 2f - 400f * Settings.scale, Settings.CREAM_COLOR);

        AbstractDungeon.overlayMenu.proceedButton.render(sb);
    }
}
