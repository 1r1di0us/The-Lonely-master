package lonelymod.companions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import lonelymod.actions.CallMoveAction;
import lonelymod.powers.CompanionStaminaPower;
import lonelymod.powers.CompanionVigorPower;
import lonelymod.powers.MeatPower;
import lonelymod.powers.TargetPower;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Meat extends AbstractCompanion {
    public static final String ID = makeID("Meat");
    public static final String IMG = makeCompanionPath("Meat.png");


    private static final int DEFAULT_PWR_AMT = 3;
    private static final int ATTACK_DMG = 6, ATTACK_AMT = 2, ATTACK_EMP_AMT = 3;
    private static final int PROTECT_BLK = 6, PROTECT_AMT = 3;
    private static final int PROTECT_PWR_AMT = 5; //we will think about adding this...
    private static final int SPECIAL_DMG = 10;
    private static final int SPECIAL_DEBUFF_AMT = 3;
    private int attackDmg;
    private int protectBlk;
    private int specialDmg;

    public Meat(float drawX, float drawY) {
        super("Meat", ID, 0.0F, 0.0F, 400.0F, 300.0F, IMG, drawX, drawY);
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.specialDmg = SPECIAL_DMG;
        this.damage.add(new DamageInfo(this, this.attackDmg));
        this.damage.add(new DamageInfo(this, this.specialDmg));
        this.block.add(new BlockInfo(this, this.protectBlk));
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new MeatPower(this), 1));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case DEFAULT:
                addToBot(new ApplyPowerAction(this, this, new CompanionVigorPower(this, DEFAULT_PWR_AMT), DEFAULT_PWR_AMT));
                addToBot(new ApplyPowerAction(this, this, new CompanionStaminaPower(this, DEFAULT_PWR_AMT), DEFAULT_PWR_AMT));
                break;
            case ATTACK:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    if (targetEnemy.hasPower(ConstrictedPower.POWER_ID))
                        addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case SPECIAL:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    addToBot(new WaitAction(0.4F));
                    addToBot(new VFXAction(new BiteEffect(targetEnemy.hb.cX +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, targetEnemy.hb.cY +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                            .cpy()), 0.0F));
                    addToBot(new DamageAction(targetEnemy, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                    if (targetEnemy.hasPower(WeakPower.POWER_ID))
                        addToBot(new ApplyPowerAction(targetEnemy, this, new ConstrictedPower(targetEnemy, this, SPECIAL_DEBUFF_AMT), SPECIAL_DEBUFF_AMT));
                }
                break;
            case UNKNOWN:
                break;
            case NONE:
                break;
        }
    }

    public void performMove(byte move) {
        switch (move) {
            case DEFAULT:
                addToTop(new ApplyPowerAction(this, this, new CompanionStaminaPower(this, DEFAULT_PWR_AMT), DEFAULT_PWR_AMT));
                addToTop(new ApplyPowerAction(this, this, new CompanionVigorPower(this, DEFAULT_PWR_AMT), DEFAULT_PWR_AMT));
                break;
            case ATTACK:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (targetEnemy.hasPower(ConstrictedPower.POWER_ID))
                        addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                    addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                break;
            case PROTECT:
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case SPECIAL:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (targetEnemy.hasPower(WeakPower.POWER_ID))
                        addToTop(new ApplyPowerAction(targetEnemy, this, new ConstrictedPower(targetEnemy, this, SPECIAL_DEBUFF_AMT), SPECIAL_DEBUFF_AMT));
                    addToTop(new DamageAction(targetEnemy, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                    addToTop(new VFXAction(new BiteEffect(targetEnemy.hb.cX +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, targetEnemy.hb.cY +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                            .cpy()), 0.0F));
                    addToTop(new WaitAction(0.4F));
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                break;
        }
    }

    @Override
    public void callDefault() {
        setMove(MOVES[0], DEFAULT, Intent.BUFF);
    }

    @Override
    public void callAttack() {
        getTarget();
        if (targetEnemy.hasPower(ConstrictedPower.POWER_ID))
            setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, ATTACK_EMP_AMT, true, true);
        else
            setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, ATTACK_AMT, true, true);
    }

    @Override
    public void callProtect() {
        setMove(MOVES[2], PROTECT, Intent.DEFEND, this.block.get(0).base, PROTECT_AMT, true, false);
    }

    @Override
    public void callSpecial() {
        getTarget();
        setMove(MOVES[3], SPECIAL, Intent.ATTACK_DEBUFF, this.damage.get(1).base, true);
    }

    @Override
    public void updateIntentTip() {
        switch (nextMove) {
            case DEFAULT:
                this.intentTip.header = MOVES[0];
                this.intentTip.body = INTENTS[0] + DEFAULT_PWR_AMT + INTENTS[1] + DEFAULT_PWR_AMT + INTENTS[2];
                this.intentTip.img = getIntentImg();
                return;
            case ATTACK:
                this.intentTip.header = MOVES[1];
                this.intentTip.body = INTENTS[3] + this.intentDmg + INTENTS[4] + ATTACK_AMT + INTENTS[5] + 1 + INTENTS[6];
                this.intentTip.img = getIntentImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                this.intentTip.body = INTENTS[7] + this.intentBlk + INTENTS[8] + PROTECT_AMT + INTENTS[9];
                this.intentTip.img = getIntentImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                this.intentTip.body = INTENTS[10] + this.intentDmg + INTENTS[11] + SPECIAL_DEBUFF_AMT + INTENTS[12];
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
                    return INTENT_TOOLTIPS[0] + this.damage.get(0).output + INTENT_TOOLTIPS[1] + ATTACK_AMT + INTENT_TOOLTIPS[2];
                }
            case PROTECT:
                if (head) {
                    return MOVES[2];
                } else {
                    return INTENT_TOOLTIPS[3] + this.block.get(0).output + INTENT_TOOLTIPS[4] + PROTECT_AMT + INTENT_TOOLTIPS[5];
                }
            case SPECIAL:
                if (head) {
                    return MOVES[3];
                } else {
                    return INTENT_TOOLTIPS[6] + this.damage.get(1).output + INTENT_TOOLTIPS[7] + SPECIAL_DEBUFF_AMT + INTENT_TOOLTIPS[8];
                }
        }
        return "";
    }

    public void useTheCard(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        return;
    }
}
