package lonelymod.orbs;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbPassiveEffect;

import basemod.abstracts.CustomOrb;
import lonelymod.actions.CompanionBasicAbilityAction;

import static lonelymod.LonelyMod.makeOrbPath;

public class StunnedAbility extends CustomOrb {

    // Standard ID/Description
    public static final String ORB_ID = makeID("StunnedAbility");
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESCRIPTIONS = orbString.DESCRIPTION;

    private static final int PASSIVE_AMOUNT = 0;
    private static final int EVOKE_AMOUNT = 0;

    // Animation Rendering Numbers - You can leave these at default, or play around with them and see what they change.
    private float vfxTimer = 1.0f;
    private float vfxIntervalMin = 0.1f;
    private float vfxIntervalMax = 0.4f;
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;
    
    public StunnedAbility() {
        super(ORB_ID, orbString.NAME, PASSIVE_AMOUNT, EVOKE_AMOUNT, DESCRIPTIONS[0], DESCRIPTIONS[1], makeOrbPath("default_orb.png"));

        updateDescription();

        angle = MathUtils.random(360.0f); // More Animation-related Numbers
        channelAnimTimer = 0.5f;
    }

    @Override
    public void updateDescription() { // Set the on-hover description of the orb
        applyFocus(); // Apply Focus (Look at the next method)
        description = DESCRIPTIONS[0];
    }

    @Override
    public void applyFocus() {
        passiveAmount = basePassiveAmount;
        evokeAmount = baseEvokeAmount;
    }

    @Override
    public void onEvoke() { // 1.On Orb Evoke
        //AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_GREMLINDOPEY_2A")); // 1. Play a Jingle Sound. Because why not
    }

    @Override
    public void onEndOfTurn() {// 1.At the end of your turn.
        //this orb will not have any effect

        //call next ability
        if (AbstractDungeon.player.hasPower(makeID("AnimalSavageryPower"))) {
            AbstractDungeon.player.getPower(makeID("AnimalSavageryPower")).onSpecificTrigger();
        }
        else {
            AbstractDungeon.actionManager.addToBottom((AbstractGameAction) new CompanionBasicAbilityAction());
        }
    }
    
    @Override
    public void updateAnimation() {// You can totally leave this as is.
        super.updateAnimation();
        angle += Gdx.graphics.getDeltaTime() * 45.0f;
        vfxTimer -= Gdx.graphics.getDeltaTime();
        if (vfxTimer < 0.0f) {
            AbstractDungeon.effectList.add(new FrostOrbPassiveEffect(cX, cY));
            vfxTimer = MathUtils.random(vfxIntervalMin, vfxIntervalMax);
        }
    }

    // Render the orb.
    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(new Color(1.0f, 1.0f, 1.0f, c.a / 2.0f));
        sb.draw(img, cX - 48.0f, cY - 48.0f + bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f, scale + MathUtils.sin(angle / PI_4) * ORB_WAVY_DIST * Settings.scale, scale, angle, 0, 0, 96, 96, false, false);
        sb.setColor(new Color(1.0f, 1.0f, 1.0f, this.c.a / 2.0f));
        sb.setBlendFunction(770, 1);
        sb.draw(img, cX - 48.0f, cY - 48.0f + bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f, scale, scale + MathUtils.sin(angle / PI_4) * ORB_WAVY_DIST * Settings.scale, -angle, 0, 0, 96, 96, false, false);
        sb.setBlendFunction(770, 771);
        renderText(sb);
        hb.render(sb);
    }

    @Override
    public void triggerEvokeAnimation() { // The evoke animation of this orb is the dark-orb activation effect.
        AbstractDungeon.effectsQueue.add(new FrostOrbActivateEffect(cX, cY));
    }

    @Override
    public void playChannelSFX() { // When you channel this orb, nothing plays
        //CardCrawlGame.sound.play("VO_GIANTHEAD_1A", 0.2f);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new StunnedAbility();
    }
}
