package lonelymod.companions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import lonelymod.cards.colorlesssummons.FeedTheBear;
import lonelymod.cards.summonmoves.*;
import lonelymod.powers.ApplyWeakNextTurnPower;
import lonelymod.powers.CompanionVigorPower;
import lonelymod.powers.MeatPower;
import lonelymod.powers.StaminaPower;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Meat extends AbstractCompanion {
    public static final String ID = makeID("Meat");
    public static final String IMG = makeCompanionPath("Meat.png");

    public static final String SHOULDER1 = makeCompanionPath("MeatShoulder.png");
    public static final String SHOULDER2 = makeCompanionPath("MeatShoulder2.png");
    public static final Texture shoulderImg = ImageMaster.loadImage(SHOULDER1);
    public static final Texture shoulder2Img = ImageMaster.loadImage(SHOULDER2);


    private static final int DEFAULT_PWR_AMT = 2, DEFAULT_BONUS_AMT = 2;
    private static final int ATTACK_DMG = 8, ATTACK_AMT = 2, ATTACK_BONUS = 1, ATTACK_BONUS_STR = 1;
    private static final int PROTECT_BLK = 5, PROTECT_AMT = 3, PROTECT_BONUS_ENERGY = 1, PROTECT_BONUS_WEAK = 1;
    private static final int SPECIAL_DMG = 25, SPECIAL_BONUS = 10;
    private int attackDmg;
    private int protectBlk;
    private int specialDmg;
    private int bonusAmt = 0; //to make it more readable. this is the amount of meatPower we should have.

    public Meat() {
        super("Meat", ID, 0.0F, 0.0F, 400.0F, 300.0F, IMG);
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.specialDmg = SPECIAL_DMG;
        this.damage.add(new DamageInfo(this, this.attackDmg, DamageInfo.DamageType.THORNS));
        this.damage.add(new DamageInfo(this, this.specialDmg, DamageInfo.DamageType.THORNS));
        this.block.add(new BlockInfo(this, this.protectBlk));

        this.cardToPreview.addAll(CardTips);
    }

    public static final ArrayList<AbstractCard> CardTips = new ArrayList<AbstractCard>() {
        {
            add(new Stretch());
            add(new Maul());
            add(new BodyBlock());
            add(new Devour());
        }
    };

    @Override
    public void usePreBattleAction() {
        addToTop(new ApplyPowerAction(this, this, new MeatPower(this, 0)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case DEFAULT:
                addToBot(new ApplyPowerAction(this, this, new CompanionVigorPower(this, DEFAULT_PWR_AMT + DEFAULT_BONUS_AMT * bonusAmt), DEFAULT_PWR_AMT + DEFAULT_BONUS_AMT * bonusAmt));
                addToBot(new ApplyPowerAction(this, this, new StaminaPower(this, DEFAULT_PWR_AMT + DEFAULT_BONUS_AMT * bonusAmt), DEFAULT_PWR_AMT + DEFAULT_BONUS_AMT * bonusAmt));
                if (hasPower(MeatPower.POWER_ID)) {
                    getPower(MeatPower.POWER_ID).onSpecificTrigger();
                }
                bonusAmt = 0;
                break;
            case ATTACK:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    if (bonusAmt > 0) {
                        for (int i = 0; i < bonusAmt; i++) {
                            addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                        }
                        addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, bonusAmt), bonusAmt));
                    }
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                    if (hasPower(MeatPower.POWER_ID)) {
                        getPower(MeatPower.POWER_ID).onSpecificTrigger();
                    }
                    bonusAmt = 0;
                }
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                if (bonusAmt > 0) {
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new EnergizedPower(AbstractDungeon.player, bonusAmt), bonusAmt));
                    addToBot(new ApplyPowerAction(this, this, new ApplyWeakNextTurnPower(this, bonusAmt), bonusAmt));
                }
                if (hasPower(MeatPower.POWER_ID)) {
                    getPower(MeatPower.POWER_ID).onSpecificTrigger();
                }
                bonusAmt = 0;
                break;
            case SPECIAL:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    talk();
                    addToBot(new WaitAction(0.4F));
                    addToBot(new VFXAction(new BiteEffect(targetEnemy.hb.cX +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, targetEnemy.hb.cY +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                            .cpy()), 0.0F));
                    DamageInfo specialDmg = new DamageInfo(this, this.damage.get(1).base + SPECIAL_BONUS * bonusAmt, this.damage.get(1).type); //kinda jank but im tired
                    addToBot(new DamageAction(targetEnemy, specialDmg, AbstractGameAction.AttackEffect.NONE));
                    addToBot(new ApplyPowerAction(targetEnemy, this, new ConstrictedPower(targetEnemy, this, specialDmg.output), specialDmg.output));
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                    if (hasPower(MeatPower.POWER_ID)) {
                        getPower(MeatPower.POWER_ID).onSpecificTrigger();
                    }
                    bonusAmt = 0;
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
                addToTop(new ApplyPowerAction(this, this, new StaminaPower(this, DEFAULT_PWR_AMT + DEFAULT_BONUS_AMT * bonusAmt), DEFAULT_PWR_AMT));
                addToTop(new ApplyPowerAction(this, this, new CompanionVigorPower(this, DEFAULT_PWR_AMT), DEFAULT_PWR_AMT));
                if (hasPower(MeatPower.POWER_ID)) {
                    getPower(MeatPower.POWER_ID).onSpecificTrigger();
                }
                bonusAmt = 0;
                break;
            case ATTACK:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        ((CompanionVigorPower) getPower(CompanionVigorPower.POWER_ID)).frenzyTrigger();
                    if (bonusAmt > 0) {
                        addToTop(new ApplyPowerAction(this, this, new StrengthPower(this, bonusAmt), bonusAmt));
                        for (int i = 0; i < bonusAmt; i++) {
                            addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                        }
                    }
                    addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    if (hasPower(MeatPower.POWER_ID)) {
                        getPower(MeatPower.POWER_ID).onSpecificTrigger();
                    }
                    bonusAmt = 0;
                }
                break;
            case PROTECT:
                if (bonusAmt > 0) {
                    addToTop(new ApplyPowerAction(this, this, new ApplyWeakNextTurnPower(this, bonusAmt), bonusAmt));
                    addToTop(new ApplyPowerAction(AbstractDungeon.player, this, new EnergizedPower(AbstractDungeon.player, bonusAmt), bonusAmt));
                }
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                if (hasPower(MeatPower.POWER_ID)) {
                    getPower(MeatPower.POWER_ID).onSpecificTrigger();
                }
                bonusAmt = 0;
                break;
            case SPECIAL:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (hasPower(MeatPower.POWER_ID)) {
                        getPower(MeatPower.POWER_ID).onSpecificTrigger();
                    }
                    talk();
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        ((CompanionVigorPower) getPower(CompanionVigorPower.POWER_ID)).frenzyTrigger();
                    DamageInfo specialDmg = new DamageInfo(this, this.damage.get(1).base + SPECIAL_BONUS * bonusAmt, this.damage.get(1).type); //kinda jank but im tired
                    addToTop(new ApplyPowerAction(targetEnemy, this, new ConstrictedPower(targetEnemy, this, specialDmg.output), specialDmg.output));
                    addToTop(new DamageAction(targetEnemy, specialDmg, AbstractGameAction.AttackEffect.NONE));
                    addToTop(new VFXAction(new BiteEffect(targetEnemy.hb.cX +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, targetEnemy.hb.cY +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                            .cpy()), 0.0F));
                    addToTop(new WaitAction(0.4F));
                    if (hasPower(MeatPower.POWER_ID)) {
                        getPower(MeatPower.POWER_ID).onSpecificTrigger();
                    }
                    bonusAmt = 0;
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
        setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, ATTACK_AMT + bonusAmt, true, true);
    }

    @Override
    public void callProtect() {
        setMove(MOVES[2], PROTECT, Intent.DEFEND, this.block.get(0).base, PROTECT_AMT, true, false);
    }

    @Override
    public void callSpecial() {
        getTarget();
        setMove(MOVES[3], SPECIAL, Intent.ATTACK_DEBUFF, this.damage.get(1).base + SPECIAL_BONUS * bonusAmt, true);
    }

    @Override
    public void updateIntentTip() {
        switch (nextMove) {
            case DEFAULT:
                this.intentTip.header = MOVES[0];
                this.intentTip.body = INTENTS[0] + DEFAULT_PWR_AMT + INTENTS[1] + DEFAULT_PWR_AMT + INTENTS[2] + DEFAULT_BONUS_AMT + INTENTS[3];
                this.intentTip.img = getIntentTipImg();
                return;
            case ATTACK:
                this.intentTip.header = MOVES[1];
                this.intentTip.body = INTENTS[4] + this.intentDmg + INTENTS[5] + ATTACK_AMT + INTENTS[6] + ATTACK_BONUS + INTENTS[7] + ATTACK_BONUS_STR + INTENTS[8];
                this.intentTip.img = getIntentTipImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                this.intentTip.body = INTENTS[9] + this.intentBlk + INTENTS[10] + PROTECT_AMT + INTENTS[11] + PROTECT_BONUS_ENERGY + INTENTS[12] + PROTECT_BONUS_WEAK + INTENTS[13];
                this.intentTip.img = getIntentTipImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                this.intentTip.body = INTENTS[14] + this.intentDmg + INTENTS[15] + SPECIAL_BONUS + INTENTS[16];
                this.intentTip.img = getIntentTipImg();
                return;
            case UNKNOWN:
                this.intentTip.header = MOVES[4];
                this.intentTip.body = INTENTS[17];
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
                    return INTENT_TOOLTIPS[0] + this.damage.get(0).output + INTENT_TOOLTIPS[1] + ATTACK_AMT + INTENT_TOOLTIPS[2] + ATTACK_BONUS + INTENT_TOOLTIPS[3] + ATTACK_BONUS_STR + INTENT_TOOLTIPS[4];
                }
            case PROTECT:
                if (head) {
                    return MOVES[2];
                } else {
                    return INTENT_TOOLTIPS[5] + this.block.get(0).output + INTENT_TOOLTIPS[6] + PROTECT_AMT + INTENT_TOOLTIPS[7] + PROTECT_BONUS_ENERGY + INTENT_TOOLTIPS[8] + PROTECT_BONUS_WEAK + INTENT_TOOLTIPS[9];
                }
            case SPECIAL:
                if (head) {
                    return MOVES[3];
                } else {
                    return INTENT_TOOLTIPS[10] + this.damage.get(1).output + INTENT_TOOLTIPS[11] + SPECIAL_BONUS + INTENT_TOOLTIPS[12];
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
        if (card instanceof FeedTheBear) {
            addToBot(new ApplyPowerAction(this, this, new MeatPower(this, 1)));
            this.bonusAmt++;
            addToTop(new MakeTempCardInHandAction(new FeedTheBear())); //to make sure none end up in the discard
            flashIntent();
            switch (this.nextMove) {
                case ATTACK:
                    setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, ATTACK_AMT + bonusAmt, true, true);
                    break;
                case SPECIAL:
                    setMove(MOVES[3], SPECIAL, Intent.ATTACK_DEBUFF, this.damage.get(1).base + SPECIAL_BONUS * bonusAmt, true);
                    break;
            }
            createIntent();
        }
    }
}
