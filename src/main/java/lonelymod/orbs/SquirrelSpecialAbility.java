package lonelymod.orbs;

import static lonelymod.ModFile.makeID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbPassiveEffect;

import basemod.abstracts.CustomOrb;
import lonelymod.powers.SquirrelDigPower;

import static lonelymod.ModFile.makeOrbPath;

public class SquirrelSpecialAbility extends CustomOrb {

    // Standard ID/Description
    public static final String ORB_ID = makeID("SquirrelSpecialAbility");
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESCRIPTIONS = orbString.DESCRIPTION;

    private static final int PASSIVE_AMOUNT = 2;
    private static final int EVOKE_AMOUNT = 0;
    private static int powerAmount = 10;
    private boolean targeted = false;
    private AbstractMonster targetMonster;
    //private static int random = -1;

    // Animation Rendering Numbers - You can leave these at default, or play around with them and see what they change.
    private float vfxTimer = 1.0f;
    private float vfxIntervalMin = 0.1f;
    private float vfxIntervalMax = 0.4f;
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;
    
    public SquirrelSpecialAbility() {
        super(ORB_ID, orbString.NAME, PASSIVE_AMOUNT, EVOKE_AMOUNT, DESCRIPTIONS[2], DESCRIPTIONS[3], makeOrbPath("default_orb.png"));

        updateDescription();

        angle = MathUtils.random(360.0f); // More Animation-related Numbers
        channelAnimTimer = 0.5f;
    }

    @Override
    public void updateDescription() { // Set the on-hover description of the orb
        applyFocus(); // Apply Focus (Look at the next method)
        description = DESCRIPTIONS[0] + passiveAmount + DESCRIPTIONS[1] + powerAmount + DESCRIPTIONS[2];
    }

    @Override
    public void applyFocus() {
        this.targetMonster = getTarget();
        if (AbstractDungeon.player.getPower("Focus") != null) {
            passiveAmount = AbstractDungeon.player.getPower("Focus").amount + basePassiveAmount;
        } else {
            passiveAmount = basePassiveAmount;
        }
        evokeAmount = baseEvokeAmount;
        if (targeted) {
            applyLockOn(this.targetMonster, this.passiveAmount);
        }
        /*passiveAmount = basePassiveAmount;
        evokeAmount = baseEvokeAmount;
        if (random < 1) {
            powerAmount = 0;
            random++;
        } else if (random == 1) {
            powerAmount = 1;
            random = -2;
        }*/
    }

    @Override
    public void onEvoke() { // 1.On Orb Evoke
        AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_GREMLINDOPEY_2A")); // 1. Play a Jingle Sound. Because why not
    }

    @Override
    public void onEndOfTurn() {// 1.At the end of your turn.
        if (targetMonster.isDeadOrEscaped()) {
            targetMonster = getTarget();
        }
        AbstractDungeon.actionManager.addToBottom(// 1.This orb will have a flare effect
            new VFXAction(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.LIGHTNING), 0.1f));
        DamageInfo info = new DamageInfo(AbstractDungeon.player, this.passiveAmount, DamageType.THORNS);
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SquirrelDigPower(AbstractDungeon.player, targetMonster, info, powerAmount), powerAmount));
        //if (powerAmount > 0) {
        //    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new IOUPower(AbstractDungeon.player, powerAmount), powerAmount));
        //}
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
            this.targeted = true;
        }
        return targetMonster;
    }

    @Override
    public void updateAnimation() {// You can totally leave this as is.
        super.updateAnimation();
        angle += Gdx.graphics.getDeltaTime() * 45.0f;
        vfxTimer -= Gdx.graphics.getDeltaTime();
        if (vfxTimer < 0.0f) {
            AbstractDungeon.effectList.add(new PlasmaOrbPassiveEffect(cX, cY));
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
        AbstractDungeon.effectsQueue.add(new LightningOrbActivateEffect(cX, cY));
    }

    @Override
    public void playChannelSFX() { // When you channel this orb, the GREMLINFAT sound plays.
        CardCrawlGame.sound.play("VO_GREMLINFAT_2B", -0.2f);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new SquirrelSpecialAbility();
    }
}
