package lonelymod.companions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import lonelymod.actions.CallMoveAction;
import lonelymod.powers.BonesPower;
import lonelymod.powers.CompanionStaminaPower;
import lonelymod.powers.CompanionVigorPower;
import lonelymod.powers.TargetPower;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Bones extends AbstractCompanion {
    public static final String ID = makeID("Bones");
    public static final String IMG = makeCompanionPath("BonesSmall.png");

    private static final int DEFAULT_BLK = 3;
    private static final int ATTACK_DMG = 8;
    private static final int ATTACK_PWR_AMT = 1;
    private static final int PROTECT_BLK = 6;
    private static final int PROTECT_PWR_AMT = 5;
    private static final int SPECIAL_DEBUFF_AMT = 3;
    private static final int SPECIAL_PWR_AMT = 3;

    private int defaultBlk;
    private int attackDmg;
    private int protectBlk;


    public Bones(float drawX, float drawY) {
        super("Bones", ID, 0.0F, 0.0F, 220.0F, 130.0F, IMG, drawX, drawY);
        this.defaultBlk = DEFAULT_BLK;
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.damage.add(new DamageInfo(this, this.attackDmg));
        this.block.add(new BlockInfo(this, this.defaultBlk));
        this.block.add(new BlockInfo(this, this.protectBlk));
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new BonesPower(this), 1));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case DEFAULT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case ATTACK:
                addToBot(new WaitAction(0.4F));
                addToBot(new VFXAction(new BiteEffect(targetEnemy.hb.cX +
                        MathUtils.random(-25.0F, 25.0F) * Settings.scale, targetEnemy.hb.cY +
                        MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                        .cpy()), 0.0F));
                addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                if (hasPower(CompanionVigorPower.POWER_ID))
                    getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, ATTACK_PWR_AMT), ATTACK_PWR_AMT));
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(1).output));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                addToBot(new ApplyPowerAction(this, this, new CompanionVigorPower(this, PROTECT_PWR_AMT), PROTECT_PWR_AMT));
                break;
            case SPECIAL:
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    addToBot(new ApplyPowerAction(mo, this, new VulnerablePower(mo, SPECIAL_DEBUFF_AMT, true), SPECIAL_DEBUFF_AMT, true, AbstractGameAction.AttackEffect.NONE));
                    addToBot(new ApplyPowerAction(mo, this, new TargetPower(mo, SPECIAL_DEBUFF_AMT, true), SPECIAL_DEBUFF_AMT, true, AbstractGameAction.AttackEffect.NONE));
                }
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT, true, AbstractGameAction.AttackEffect.NONE));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT, true, AbstractGameAction.AttackEffect.NONE));
                break;
            case UNKNOWN:
                break;
            case NONE:
                break;
        }
    }

    public void performTurn(byte move) {
        switch (move) {
            case DEFAULT:
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case ATTACK:
                addToTop(new ApplyPowerAction(this, this, new StrengthPower(this, ATTACK_PWR_AMT), ATTACK_PWR_AMT));
                addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                addToTop(new VFXAction(new BiteEffect(targetEnemy.hb.cX +
                        MathUtils.random(-25.0F, 25.0F) * Settings.scale, targetEnemy.hb.cY +
                        MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                        .cpy()), 0.0F));
                addToTop(new WaitAction(0.4F));
                if (hasPower(CompanionVigorPower.POWER_ID))
                    getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                break;
            case PROTECT:
                addToTop(new ApplyPowerAction(this, this, new CompanionVigorPower(this, PROTECT_PWR_AMT), PROTECT_PWR_AMT));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(1).output));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case SPECIAL:
                addToTop(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT, true, AbstractGameAction.AttackEffect.NONE));
                addToTop(new ApplyPowerAction(this, this, new StrengthPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT, true, AbstractGameAction.AttackEffect.NONE));
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    addToTop(new ApplyPowerAction(mo, this, new TargetPower(mo, SPECIAL_DEBUFF_AMT, true), SPECIAL_DEBUFF_AMT, true, AbstractGameAction.AttackEffect.NONE));
                    addToTop(new ApplyPowerAction(mo, this, new VulnerablePower(mo, SPECIAL_DEBUFF_AMT, true), SPECIAL_DEBUFF_AMT, true, AbstractGameAction.AttackEffect.NONE));
                }
                break;
        }
    }

    @Override
    public void callDefault() {
        setMove(MOVES[0], DEFAULT, Intent.DEFEND, this.block.get(0).base, false);
    }

    @Override
    public void callAttack() {
        getTarget();
        setMove(MOVES[1], ATTACK, Intent.ATTACK_BUFF, this.damage.get(0).base, true);
    }

    @Override
    public void callProtect() {
        setMove(MOVES[2], PROTECT, Intent.DEFEND_BUFF, this.block.get(1).base, false);
    }

    @Override
    public void callSpecial() {
        setMove(MOVES[3], SPECIAL, Intent.STRONG_DEBUFF);
    }

    @Override
    public void updateIntentTip() {
        switch (nextMove) {
            case DEFAULT:
                this.intentTip.header = MOVES[0];
                this.intentTip.body = INTENTS[0] + this.intentBlk + INTENTS[1];
                this.intentTip.img = getIntentImg();
                return;
            case ATTACK:
                this.intentTip.header = MOVES[1];
                this.intentTip.body = INTENTS[2] + this.intentDmg + INTENTS[3] + ATTACK_PWR_AMT + INTENTS[4];
                this.intentTip.img = getIntentImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                this.intentTip.body = INTENTS[5] + this.intentBlk + INTENTS[6] + PROTECT_PWR_AMT + INTENTS[7];
                this.intentTip.img = getIntentImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                this.intentTip.body = INTENTS[8] + SPECIAL_DEBUFF_AMT + INTENTS[9] + SPECIAL_DEBUFF_AMT + INTENTS[10] + SPECIAL_PWR_AMT + INTENTS[11] + SPECIAL_PWR_AMT + INTENTS[12];
                this.intentTip.img = getIntentImg();
                return;
            case UNKNOWN:
                this.intentTip.header = MOVES[4];
                this.intentTip.body = INTENTS[13];
                this.intentTip.img = getIntentImg();
                return;
            case NONE:
                this.intentTip.header = "";
                this.intentTip.body = "";
                this.intentTip.img = null;
                return;
        }
        this.intentTip.header = "NOT SET";
        this.intentTip.body = "NOT SET";
        this.intentTip.img = ImageMaster.INTENT_UNKNOWN;
    }

    public String getKeywordMoveTip(byte move, boolean head) {
        switch (move) {
            case ATTACK:
                if (head) {
                    return MOVES[1];
                } else {
                    return INTENT_TOOLTIPS[0] + this.damage.get(0).output + INTENT_TOOLTIPS[1] + ATTACK_PWR_AMT + INTENT_TOOLTIPS[2];
                }
            case PROTECT:
                if (head) {
                    return MOVES[2];
                } else {
                    return INTENT_TOOLTIPS[3] + this.block.get(1).output + INTENT_TOOLTIPS[4] + PROTECT_PWR_AMT + INTENT_TOOLTIPS[5];
                }
            case SPECIAL:
                if (head) {
                    return MOVES[3];
                } else {
                    return INTENT_TOOLTIPS[6] + SPECIAL_DEBUFF_AMT + INTENT_TOOLTIPS[7] + SPECIAL_DEBUFF_AMT + INTENT_TOOLTIPS[8] + SPECIAL_PWR_AMT + INTENT_TOOLTIPS[9] + SPECIAL_PWR_AMT + INTENT_TOOLTIPS[10];
                }
        }
        return "";
    }
}
