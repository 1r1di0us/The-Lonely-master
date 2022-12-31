package lonelymod.orbs;

import static lonelymod.ModFile.makeID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbPassiveEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;

import basemod.abstracts.CustomOrb;

import static lonelymod.ModFile.makeOrbPath;

public class BearAttackAbility extends CustomOrb {

    // Standard ID/Description
    public static final String ORB_ID = makeID("BearAttackAbility");
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESCRIPTIONS = orbString.DESCRIPTION;

    private static final int PASSIVE_AMOUNT = 5;
    private static final int EVOKE_AMOUNT = 0;

    private boolean targeted = false;
    private AbstractMonster targetMonster;


    // Animation Rendering Numbers - You can leave these at default, or play around with them and see what they change.
    private float vfxTimer = 1.0f;
    private float vfxIntervalMin = 0.1f;
    private float vfxIntervalMax = 0.4f;
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;
    
    public BearAttackAbility() {
        super(ORB_ID, orbString.NAME, PASSIVE_AMOUNT, EVOKE_AMOUNT, DESCRIPTIONS[2], DESCRIPTIONS[3], makeOrbPath("default_orb.png"));

        updateDescription();

        angle = MathUtils.random(360.0f); // More Animation-related Numbers
        channelAnimTimer = 0.5f;
    }

    @Override
    public void updateDescription() { // Set the on-hover description of the orb
        applyFocus(); // Apply Focus (Look at the next method)
        description = DESCRIPTIONS[0] + passiveAmount + DESCRIPTIONS[1] + passiveAmount + DESCRIPTIONS[2];
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
    }

    @Override
    public void onEvoke() { // 1.On Orb Evoke
        AbstractDungeon.actionManager.addToBottom(new SFXAction("SLIME_BLINK_1")); // 1. Play a Jingle Sound. Because why not
    }

    @Override
    public void onEndOfTurn() {// 1.At the end of your turn.
        AbstractDungeon.actionManager.addToBottom(// 1.This orb will have a flare effect
            new VFXAction(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.PLASMA), 0.1f));
        AbstractDungeon.actionManager.addToBottom(// 2. And deal damage
            new DamageAction(this.targetMonster, new DamageInfo(AbstractDungeon.player, this.passiveAmount), AttackEffect.BLUNT_HEAVY));
        AbstractDungeon.actionManager.addToBottom(// deal damage
            new DamageAction(this.targetMonster, new DamageInfo(AbstractDungeon.player, this.passiveAmount), AttackEffect.BLUNT_HEAVY));
        if (targetMonster.hasPower("Weak")) {
            AbstractDungeon.actionManager.addToBottom(// deal damage
                new DamageAction(this.targetMonster, new DamageInfo(AbstractDungeon.player, this.passiveAmount), AttackEffect.BLUNT_HEAVY));
        }
    }
    
    private AbstractMonster getTarget() {
        int target = 0;
        AbstractMonster targetMonster = null;
        for (AbstractMonster m: (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (m.hasPower(LockOnPower.POWER_ID)) {
                if (target < m.getPower(LockOnPower.POWER_ID).amount) {
                    target = m.getPower(LockOnPower.POWER_ID).amount;
                    targetMonster = m;
                }
            }
        }
        if (target == 0)
            targetMonster = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
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
    public void playChannelSFX() { // When you channel this orb, the GREMLINFAT sound plays.
        CardCrawlGame.sound.play("VO_GIANTHEAD_1B", 0.1f);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new BearAttackAbility();
    }
}
