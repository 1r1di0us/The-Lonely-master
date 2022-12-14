package lonelymod.orbs;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbPassiveEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;

import basemod.abstracts.CustomOrb;
import lonelymod.actions.CompanionBasicAbilityAction;

import static lonelymod.LonelyMod.makeOrbPath;

public class ByrdAttackAbility extends CustomOrb {

    // Standard ID/Description
    public static final String ORB_ID = makeID("ByrdAttackAbility");
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESCRIPTIONS = orbString.DESCRIPTION;

    private static final int PASSIVE_AMOUNT = 1;
    private static final int EVOKE_AMOUNT = 0;

    private AbstractMonster targetMonster;
    private int peckAmount;

    // Animation Rendering Numbers - You can leave these at default, or play around with them and see what they change.
    private float vfxTimer = 1.0f;
    private float vfxIntervalMin = 0.1f;
    private float vfxIntervalMax = 0.4f;
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;

    public ByrdAttackAbility() {
        super(ORB_ID, orbString.NAME, PASSIVE_AMOUNT, EVOKE_AMOUNT, DESCRIPTIONS[2], DESCRIPTIONS[3], makeOrbPath("default_orb.png"));

        updateDescription();

        angle = MathUtils.random(360.0f); // More Animation-related Numbers
        channelAnimTimer = 0.5f;
    }

    @Override
    public void updateDescription() { // Set the on-hover description of the orb
        applyFocus(); // Apply Focus (Look at the next method)
        if (AbstractDungeon.player.hasPower(makeID("OmenPower"))) {
            peckAmount = AbstractDungeon.player.getPower(makeID("OmenPower")).amount;
        } else {
            peckAmount = 0;
        }
        description = DESCRIPTIONS[0] + passiveAmount + DESCRIPTIONS[1] + peckAmount + DESCRIPTIONS[2];
    }

    @Override
    public void applyFocus() {
        if (AbstractDungeon.player.getPower("Focus") != null) {
            passiveAmount = AbstractDungeon.player.getPower("Focus").amount + basePassiveAmount;
        } else {
            passiveAmount = basePassiveAmount;
        }
        evokeAmount = baseEvokeAmount;
    }

    @Override
    public void onEvoke() { // 1.On Orb Evoke
        AbstractDungeon.actionManager.addToBottom(new SFXAction("THUNDERCLAP")); // 1. Play a thunder Sound. Because why not
        // For a list of sound effects you can use, look under com.megacrit.cardcrawl.audio.SoundMaster - you can see the list of keys you can use there. As far as previewing what they sound like, open desktop-1.0.jar with something like 7-Zip and go to audio. Reference the file names provided. (Thanks fiiiiilth)

    }

    @Override
    public void onEndOfTurn() {// 1.At the end of your turn.{
        targetMonster = getTarget();
        if (targetMonster.hasPower(makeID("Fetch"))) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(targetMonster, targetMonster, makeID("FetchPower")));
        }
        AbstractDungeon.actionManager.addToBottom(// 1.This orb will have a flare effect
                new VFXAction(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.DARK), 0.1f));
        for (int i = 0; i < peckAmount; i++) {
            AbstractDungeon.actionManager.addToBottom(// 2. And deal damage
                new DamageAction(this.targetMonster, new DamageInfo(AbstractDungeon.player, this.passiveAmount), AttackEffect.BLUNT_LIGHT));
        }

        //call next ability
        if (AbstractDungeon.player.hasPower(makeID("AnimalSavageryPower"))) {
            AbstractDungeon.player.getPower(makeID("AnimalSavageryPower")).onSpecificTrigger();
        }
        else {
            AbstractDungeon.actionManager.addToBottom(new CompanionBasicAbilityAction());
        }
    }

    private AbstractMonster getTarget() {
        int target = 0;
        AbstractMonster targetMonster = null;
        for (AbstractMonster m: (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (!m.isDeadOrEscaped() && m.hasPower(LockOnPower.POWER_ID)) {
                if (target < m.getPower(LockOnPower.POWER_ID).amount) {
                    target = m.getPower(LockOnPower.POWER_ID).amount;
                    targetMonster = m;
                }
            }
        }
        if (target == 0) {
            while (targetMonster == null || targetMonster.isDeadOrEscaped())
                targetMonster = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        }
        else {
            this.passiveAmount = applyLockOn(targetMonster, this.passiveAmount);
        }
        return targetMonster;
    }

    @Override
    public void updateAnimation() {// You can totally leave this as is.
        // If you want to create a whole new orb effect - take a look at conspire's Water Orb. It includes a custom sound, too!
        super.updateAnimation();
        angle += Gdx.graphics.getDeltaTime() * 45.0f;
        vfxTimer -= Gdx.graphics.getDeltaTime();
        if (vfxTimer < 0.0f) {
            AbstractDungeon.effectList.add(new LightningOrbPassiveEffect(cX, cY)); // This is the purple-sparkles in the orb. You can change this to whatever fits your orb.
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
        AbstractDungeon.effectsQueue.add(new PlasmaOrbActivateEffect(cX, cY));
    }

    @Override
    public void playChannelSFX() { // When you channel this orb, play the sound. VO_CULTIST has 6 sounds, 1A, 1B, 1C, 2A, 2B, and 2C
        CardCrawlGame.sound.play("VO_CULTIST_1B", 0.1f);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new ByrdAttackAbility();
    }
}