package lonelymod.companions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.random.Random;
import lonelymod.LonelyMod;
import lonelymod.cards.summonmoves.*;
import lonelymod.fields.CompanionField;
import lonelymod.powers.BonesPower;
import lonelymod.powers.CompanionVigorPower;
import lonelymod.powers.StaminaPower;
import lonelymod.powers.TargetPower;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Bones extends AbstractCompanion {
    public static final String ID = makeID("Bones");
    public static final String IMG = makeCompanionPath("Bones.png");

    public static final String SHOULDER1 = makeCompanionPath("BonesShoulder.png");
    public static final String SHOULDER2 = makeCompanionPath("BonesShoulder2.png");
    public static final Texture shoulderImg = ImageMaster.loadImage(SHOULDER1);
    public static final Texture shoulder2Img = ImageMaster.loadImage(SHOULDER2);


    private static final int DEFAULT_BLK = 3;
    private static final int ATTACK_DMG = 10;
    private static final int ATTACK_PWR_AMT = 2;
    private static final int PROTECT_BLK = 4;
    private static final int PROTECT_AMT = 2;
    private static final int PROTECT_PWR_AMT = 4;
    private static final int SPECIAL_SELF_PWR_AMT = 5;
    private static final int SPECIAL_PWR_AMT = 3;
    private static final int SPECIAL_DEBUFF_AMT = 3;


    private int defaultBlk;
    private int attackDmg;
    private int protectBlk;


    public Bones() {
        super("Bones", ID, 0.0F, 0.0F, 220.0F, 130.0F, IMG);

        this.defaultBlk = DEFAULT_BLK;
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.damage.add(new DamageInfo(this, this.attackDmg, DamageInfo.DamageType.THORNS));
        this.block.add(new BlockInfo(this, this.defaultBlk));
        this.block.add(new BlockInfo(this, this.protectBlk));

        this.cardToPreview.addAll(CardTips);
    }

    public static final ArrayList<AbstractCard> CardTips = new ArrayList<AbstractCard>() {
        {
            add(new Bark());
            add(new Bite());
            add(new Poise());
            add(new Howl());
        }
    };

    @Override
    public void useOnSummonAction(boolean onBattleStart) {
        CompanionField.playableCards.set(AbstractDungeon.player, null);
        addToTop(new ApplyPowerAction(this, this, new BonesPower(this), -1));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case DEFAULT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case ATTACK:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) { //have to check ABSOLUTELY EVERYWHERE IN CASE ITS AWAKENED ONE
                    addToBot(new WaitAction(0.4F));
                    addToBot(new VFXAction(new BiteEffect(targetEnemy.hb.cX +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, targetEnemy.hb.cY +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                            .cpy()), 0.0F));
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, ATTACK_PWR_AMT), ATTACK_PWR_AMT));
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(1).output));
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(1).output));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                addToBot(new ApplyPowerAction(this, this, new CompanionVigorPower(this, PROTECT_PWR_AMT), PROTECT_PWR_AMT));
                break;
            case SPECIAL:
                talk();
                addToBot(new SFXAction(LonelyMod.HOWL_SFX_1));
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (mo != null && !mo.isDeadOrEscaped()) {
                        addToBot(new ApplyPowerAction(mo, this, new VulnerablePower(mo, SPECIAL_DEBUFF_AMT, true), SPECIAL_DEBUFF_AMT, true, AbstractGameAction.AttackEffect.NONE));
                        addToBot(new ApplyPowerAction(mo, this, new TargetPower(mo, SPECIAL_DEBUFF_AMT, true), SPECIAL_DEBUFF_AMT, true, AbstractGameAction.AttackEffect.NONE));
                    }
                }
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, SPECIAL_SELF_PWR_AMT), SPECIAL_SELF_PWR_AMT, true, AbstractGameAction.AttackEffect.NONE));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT, true, AbstractGameAction.AttackEffect.NONE));
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
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case ATTACK:
                addToTop(new ApplyPowerAction(this, this, new StrengthPower(this, ATTACK_PWR_AMT), ATTACK_PWR_AMT));
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        ((CompanionVigorPower) getPower(CompanionVigorPower.POWER_ID)).frenzyTrigger();
                    addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                    addToTop(new VFXAction(new BiteEffect(targetEnemy.hb.cX +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, targetEnemy.hb.cY +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                            .cpy()), 0.0F));
                    addToTop(new WaitAction(0.4F));
                }
                break;
            case PROTECT:
                addToTop(new ApplyPowerAction(this, this, new CompanionVigorPower(this, PROTECT_PWR_AMT), PROTECT_PWR_AMT));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(1).output));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(1).output));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case SPECIAL:
                talk();
                addToTop(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT, true, AbstractGameAction.AttackEffect.NONE));
                addToTop(new ApplyPowerAction(this, this, new StrengthPower(this, SPECIAL_SELF_PWR_AMT), SPECIAL_SELF_PWR_AMT, true, AbstractGameAction.AttackEffect.NONE));
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (mo != null && !mo.isDeadOrEscaped()) {
                        addToTop(new ApplyPowerAction(mo, this, new TargetPower(mo, SPECIAL_DEBUFF_AMT, true), SPECIAL_DEBUFF_AMT, true, AbstractGameAction.AttackEffect.NONE));
                        addToTop(new ApplyPowerAction(mo, this, new VulnerablePower(mo, SPECIAL_DEBUFF_AMT, true), SPECIAL_DEBUFF_AMT, true, AbstractGameAction.AttackEffect.NONE));
                    }
                }
                addToTop(new SFXAction(LonelyMod.HOWL_SFX_1));
                break;
        }
    }

    @Override
    public void callDefault() {
        setMove(MOVES[0], DEFAULT, Intent.DEFEND, this.block.get(0).base, false);
    }

    @Override
    public void callAttack() {
        /*if (hasPower(BonesPower.POWER_ID)) {
            getPower(BonesPower.POWER_ID).onSpecificTrigger();
        }*/
        getTarget();
        setMove(MOVES[1], ATTACK, Intent.ATTACK_BUFF, this.damage.get(0).base, true);
    }

    @Override
    public void callProtect() {
        /*if (hasPower(BonesPower.POWER_ID)) {
            getPower(BonesPower.POWER_ID).onSpecificTrigger();
        }*/
        setMove(MOVES[2], PROTECT, Intent.DEFEND_BUFF, this.block.get(1).base, PROTECT_AMT, true, false);
    }

    @Override
    public void callSpecial() {
        /*if (hasPower(BonesPower.POWER_ID)) {
            getPower(BonesPower.POWER_ID).onSpecificTrigger();
        }*/
        setMove(MOVES[3], SPECIAL, Intent.STRONG_DEBUFF);
    }

    @Override
    public void updateIntentTip() {
        switch (nextMove) {
            case DEFAULT:
                this.intentTip.header = MOVES[0];
                this.intentTip.body = INTENTS[0] + this.intentBlk + INTENTS[1];
                this.intentTip.img = getIntentTipImg();
                return;
            case ATTACK:
                this.intentTip.header = MOVES[1];
                this.intentTip.body = INTENTS[2] + this.intentDmg + INTENTS[3] + ATTACK_PWR_AMT + INTENTS[4];
                this.intentTip.img = getIntentTipImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                this.intentTip.body = INTENTS[5] + this.intentBlk + INTENTS[6] + PROTECT_AMT + INTENTS[7] + PROTECT_PWR_AMT + INTENTS[8];
                this.intentTip.img = getIntentTipImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                this.intentTip.body = INTENTS[9] + SPECIAL_DEBUFF_AMT + INTENTS[10] + SPECIAL_DEBUFF_AMT + INTENTS[11] + SPECIAL_SELF_PWR_AMT + INTENTS[12] + SPECIAL_PWR_AMT + INTENTS[13];
                this.intentTip.img = getIntentTipImg();
                return;
            case UNKNOWN:
                this.intentTip.header = MOVES[4];
                this.intentTip.body = INTENTS[14];
                this.intentTip.img = getIntentTipImg();
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
                    return INTENT_TOOLTIPS[3] + this.block.get(1).output + INTENT_TOOLTIPS[4] + PROTECT_AMT + INTENT_TOOLTIPS[5] + PROTECT_PWR_AMT + INTENT_TOOLTIPS[6];
                }
            case SPECIAL:
                if (head) {
                    return MOVES[3];
                } else {
                    return INTENT_TOOLTIPS[7] + SPECIAL_DEBUFF_AMT + INTENT_TOOLTIPS[8] + SPECIAL_DEBUFF_AMT + INTENT_TOOLTIPS[9] + SPECIAL_SELF_PWR_AMT + INTENT_TOOLTIPS[10] + SPECIAL_PWR_AMT + INTENT_TOOLTIPS[11];
                }
        }
        return "";
    }

    @Override
    public void talk() {
        Random rand = new Random();
        int text = -1;
        if (lastDialog == -1)
            text = rand.random(0,2);
        else {
            text = rand.random(0, 1);
            if (lastDialog <= text)
                text++;
        }
        AbstractDungeon.effectList.add(new SpeechBubble(this.dialogX, this.dialogY, 3.0F, DIALOG[text], true));
    }

    public void useTheCard(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        return;
    }
}
