package lonelymod.companions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import lonelymod.cards.colorlesssummons.FeedTheBear;
import lonelymod.cards.summonmoves.*;
import lonelymod.fields.CompanionField;
import lonelymod.powers.*;
import lonelymod.relics.PaperDaug;

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


    private static final int DEFAULT_PWR_AMT = 2, DEFAULT_ABILITY_AMT = 2;
    private static final int ATTACK_DMG = 6, ATTACK_AMT = 2, ATTACK_BONUS = 1, ATTACK_ABILITY_MAX_STR = 4;
    private static final int PROTECT_BLK = 4, PROTECT_AMT = 3, PROTECT_ABILITY_VIGOR = 4, PROTECT_ABILITY_WEAK = 1;
    private static final int SPECIAL_DMG = 12, SPECIAL_ABILITY_ENERGY = 2, SPECIAL_ABILITY_ATTACK = 1;
    private int attackDmg;
    private int protectBlk;
    private int specialDmg;

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
    public void useOnSummonAction(boolean onBattleStart) {
        CompanionField.playableCards.set(AbstractDungeon.player, new ArrayList<>());
        CompanionField.playableCards.get(AbstractDungeon.player).add(new FeedTheBear());
        addToTop(new ApplyPowerAction(this, this, new MeatPower(this, 0)));
        addToBot(new MakeTempCardInHandAction(new FeedTheBear()));
    }

    @Override
    public void performTurn() {
        switch (this.nextMove) {
            case DEFAULT:
                addToBot(new ApplyPowerAction(this, this, new CompanionVigorPower(this, DEFAULT_PWR_AMT), DEFAULT_PWR_AMT));
                addToBot(new ApplyPowerAction(this, this, new StaminaPower(this, DEFAULT_PWR_AMT), DEFAULT_PWR_AMT));
                break;
            case ATTACK:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    boolean bonus = false;
                    for (AbstractPower pow : targetEnemy.powers) if (pow.type == AbstractPower.PowerType.DEBUFF) {
                        bonus = true;
                        break;
                    }
                    if (bonus) {
                        for (int i = 0; i < ATTACK_BONUS; i++) addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                    }
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case SPECIAL:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    addToBot(new WaitAction(0.4F));
                    addToBot(new VFXAction(new BiteEffect(targetEnemy.hb.cX +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, targetEnemy.hb.cY +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                            .cpy()), 0.0F));
                    addToBot(new DamageAction(targetEnemy, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                    addToBot(new ApplyPowerAction(targetEnemy, this, new ConstrictedPower(targetEnemy, this, (this.damage.get(1).output + 1) / 2))); // why the fuck did they delete Math.ceilDiv???
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                    addToBot(new ApplyPowerAction(this, this, new CompanionVigorPower(this, (this.damage.get(1).output + 1) / 2)));
                }
                break;
        }
    }

    public void performImmediately(byte move) {
        switch (move) {
            case DEFAULT:
                addToTop(new ApplyPowerAction(this, this, new StaminaPower(this, DEFAULT_PWR_AMT), DEFAULT_PWR_AMT));
                addToTop(new ApplyPowerAction(this, this, new CompanionVigorPower(this, DEFAULT_PWR_AMT), DEFAULT_PWR_AMT));
                break;
            case ATTACK:
                getTarget();
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        ((CompanionVigorPower) getPower(CompanionVigorPower.POWER_ID)).instantTrigger();
                    boolean bonus = false;
                    for (AbstractPower pow : targetEnemy.powers) if (pow.type == AbstractPower.PowerType.DEBUFF) {
                        bonus = true;
                        break;
                    }
                    if (bonus) {
                        for (int i = 0; i < ATTACK_BONUS; i++) addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                    }
                    addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                break;
            case PROTECT:
                if (hasPower(StaminaPower.POWER_ID))
                    ((StaminaPower) getPower(StaminaPower.POWER_ID)).instantTrigger();
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                break;
            case SPECIAL:
                getTarget();
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    int output = getDamageOutput(this.damage.get(1).base, this.damage.get(1).type, targetEnemy);
                    addToTop(new ApplyPowerAction(this, this, new CompanionVigorPower(this, (output + 1) / 2))); // why the fuck did they remove ceiling division?!?!?
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        ((CompanionVigorPower) getPower(CompanionVigorPower.POWER_ID)).instantTrigger();
                    addToTop(new ApplyPowerAction(targetEnemy, this, new ConstrictedPower(targetEnemy, this, (this.damage.get(1).output + 1) / 2)));
                    addToTop(new DamageAction(targetEnemy, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                    addToTop(new VFXAction(new BiteEffect(targetEnemy.hb.cX +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, targetEnemy.hb.cY +
                            MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                            .cpy()), 0.0F));
                    addToTop(new WaitAction(0.4F));
                }
                break;
        }
    }

    private int getDamageOutput(int base, DamageInfo.DamageType type, AbstractCreature target) {
        int output = base; // for getting the damage of the special if its being instaperformed.
        float tmp = output;
        for (AbstractPower p : this.powers) {
            tmp = p.atDamageGive(tmp, type);
        }
        for (AbstractPower p : target.powers) {
            if (!(p instanceof VulnerablePower)) {
                tmp = p.atDamageReceive(tmp, type);
            }
        }
        for (AbstractPower p : this.powers) {
            tmp = p.atDamageFinalGive(tmp, type);
        }
        for (AbstractPower p : target.powers) {
            tmp = p.atDamageFinalReceive(tmp, type);
        }
        if (isTargeted && target.hasPower(TargetPower.POWER_ID))
            if (AbstractDungeon.player.hasRelic(PaperDaug.ID))
                tmp = (int)(tmp * PaperDaug.MULT);
            else
                tmp = (int)(tmp * 1.5F);
        output = MathUtils.floor(tmp);
        if (output < 0)
            output = 0;
        return output;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (nextMove == AbstractCompanion.ATTACK && move.damageMultiplier == 2 && targetEnemy != null && !targetEnemy.isDeadOrEscaped()) { // refresh attack in case you apply a debuff
            for (AbstractPower pow : targetEnemy.powers) if (pow.type == AbstractPower.PowerType.DEBUFF) {
                callMove(AbstractCompanion.ATTACK, false, true, false);
                break;
            }
        }
    }

    public void setupMove(byte move, boolean allowRetarget) {
        switch (move) {
            case DEFAULT:
                setMove(MOVES[0], DEFAULT, Intent.BUFF);
                break;
            case ATTACK:
                if (allowRetarget) getTarget();
                int bonus = 0;
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    for (AbstractPower pow : targetEnemy.powers) if (pow.type == AbstractPower.PowerType.DEBUFF) {
                            bonus = ATTACK_BONUS;
                            break;
                        }
                }
                setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, ATTACK_AMT + bonus, true, true);
                break;
            case PROTECT:
                setMove(MOVES[2], PROTECT, Intent.DEFEND, this.block.get(0).base, PROTECT_AMT, true, false);
                break;
            case SPECIAL:
                if (allowRetarget) getTarget();
                setMove(MOVES[3], SPECIAL, Intent.ATTACK_DEBUFF, this.damage.get(1).base, true);
                break;
        }
    }

    @Override
    public void updateIntentTip() {
        switch (nextMove) {
            case DEFAULT:
                this.intentTip.header = MOVES[0];
                this.intentTip.body = INTENTS[0] + DEFAULT_PWR_AMT + INTENTS[1] + DEFAULT_PWR_AMT + INTENTS[2] + INTENTS[3] + DEFAULT_ABILITY_AMT + INTENTS[1] + DEFAULT_ABILITY_AMT + INTENTS[2];
                this.intentTip.img = getIntentTipImg();
                return;
            case ATTACK:
                this.intentTip.header = MOVES[1];
                int bonus = 0;
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    for (AbstractPower pow : targetEnemy.powers) if (pow.type == AbstractPower.PowerType.DEBUFF) {
                        bonus = ATTACK_BONUS;
                        break;
                    }
                }
                this.intentTip.body = INTENTS[4] + this.intentDmg + INTENTS[5] + (ATTACK_AMT+bonus) + INTENTS[6] + ATTACK_BONUS + INTENTS[7] + ATTACK_ABILITY_MAX_STR + INTENTS[8];
                this.intentTip.img = getIntentTipImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                this.intentTip.body = INTENTS[9] + this.intentBlk + INTENTS[10] + PROTECT_AMT + INTENTS[11] + PROTECT_ABILITY_VIGOR + INTENTS[12] + PROTECT_ABILITY_WEAK + INTENTS[13];
                this.intentTip.img = getIntentTipImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                this.intentTip.body = INTENTS[14] + this.intentDmg + INTENTS[15] + SPECIAL_ABILITY_ENERGY + INTENTS[16];
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
                    return INTENT_TOOLTIPS[0] + this.damage.get(0).output + INTENT_TOOLTIPS[1] + ATTACK_AMT + INTENT_TOOLTIPS[2] + ATTACK_BONUS + INTENT_TOOLTIPS[3];
                }
            case PROTECT:
                if (head) {
                    return MOVES[2];
                } else {
                    return INTENT_TOOLTIPS[4] + this.block.get(0).output + INTENT_TOOLTIPS[5] + PROTECT_AMT + INTENT_TOOLTIPS[6];
                }
            case SPECIAL:
                if (head) {
                    return MOVES[3];
                } else {
                    return INTENT_TOOLTIPS[7] + this.damage.get(1).output + INTENT_TOOLTIPS[8];
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
            addToTop(new MakeTempCardInHandAction(new FeedTheBear())); //to make sure none end up in the discard
            //flashIntent();
            switch (this.nextMove) {
                case DEFAULT:
                    addToBot(new ApplyPowerAction(this, this, new CompanionVigorPower(this, DEFAULT_ABILITY_AMT)));
                    addToBot(new ApplyPowerAction(this, this, new StaminaPower(this, DEFAULT_ABILITY_AMT)));
                    break;
                case ATTACK:
                    if (hasPower(CompanionVigorPower.POWER_ID) && getPower(CompanionVigorPower.POWER_ID).amount > 0) { //can never be too careful
                        int strAmount = 0;
                        if (getPower(CompanionVigorPower.POWER_ID).amount >= ATTACK_ABILITY_MAX_STR) {
                            strAmount = ATTACK_ABILITY_MAX_STR;
                        } else {
                            strAmount = getPower(CompanionVigorPower.POWER_ID).amount;
                        }
                        if (strAmount == 0) {
                            AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, DIALOG[3], true));
                        } else {
                        addToBot(new ReducePowerAction(this, this, getPower(CompanionVigorPower.POWER_ID), strAmount));
                        addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, strAmount)));
                        }
                    }
                    break;
                case PROTECT:
                    addToBot(new ApplyPowerAction(this, this, new CompanionVigorPower(this, PROTECT_ABILITY_VIGOR)));
                    addToBot(new ApplyPowerAction(this, this, new ApplyWeakNextTurnPower(this, PROTECT_ABILITY_WEAK)));
                    break;
                case SPECIAL:
                    if (targetEnemy == null || targetEnemy.isDeadOrEscaped()) {
                        getTarget();
                    }
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new EnergizedPower(AbstractDungeon.player, SPECIAL_ABILITY_ENERGY)));
                    addToBot(new ApplyPowerAction(this, this, new AttackNextTurnPower(this, SPECIAL_ABILITY_ATTACK)));
                    break;
            }
            createIntent();
        }
    }
}
