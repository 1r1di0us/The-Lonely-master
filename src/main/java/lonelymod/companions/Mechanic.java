package lonelymod.companions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import lonelymod.powers.*;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Mechanic extends AbstractCompanion {
    public static final String ID = makeID("Mechanic");
    public static final String IMG = makeCompanionPath("Mechanic.png");

    private static final int ATTACK_DMG = 8;
    private static final int PROTECT_BLK = 8;
    private static final int SPECIAL_PWR_AMT = 1;

    private int attackDmg;
    private int protectBlk;

    public Mechanic(float drawX, float drawY) {
        super("Blirt", ID, 0.0F, 0.0F, 90.0F, 120.0F, IMG, drawX, drawY);
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.damage.add(new DamageInfo(this, this.attackDmg));
        this.block.add(new BlockInfo(this, this.protectBlk));
    }

    @Override
    public void usePreBattleAction() {
        addToTop(new ApplyPowerAction(this, this, new MechanicPower(this)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case DEFAULT:
                int roll = MathUtils.random(1);
                if (roll == 0) {
                    addToBot(new SFXAction("VO_GREMLINCALM_1A"));
                } else {
                    addToBot(new SFXAction("VO_GREMLINCALM_1B"));
                }
                break;
            case ATTACK:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.LIGHTNING));
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                addToBot(new ChannelAction(new Lightning()));
                if (hasPower(RoboArmPower.POWER_ID)) {
                    for (int i = 0; i < getPower(RoboArmPower.POWER_ID).amount; i++) {
                        if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                            addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.LIGHTNING));
                        }
                        addToBot(new ChannelAction(new Lightning()));
                    }
                }
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                addToBot(new ChannelAction(new Frost()));
                if (hasPower(RoboArmPower.POWER_ID)) {
                    for (int i = 0; i < getPower(RoboArmPower.POWER_ID).amount; i++) {
                        addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                        addToBot(new ChannelAction(new Frost()));
                    }
                }
                break;
            case SPECIAL:
                if (hasPower(RoboArmPower.POWER_ID)) {
                    int amount = getPower(RoboArmPower.POWER_ID).amount;
                    for (int i = 0; i < amount; i++) {
                        addToBot(new ApplyPowerAction(this, this, new RoboArmPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT));
                    }
                }
                addToBot(new ApplyPowerAction(this, this, new RoboArmPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT));
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
                int roll = MathUtils.random(1);
                if (roll == 0) {
                    addToBot(new SFXAction("VO_GREMLINDOPEY_1A"));
                } else {
                    addToBot(new SFXAction("VO_GREMLINDOPEY_1B"));
                }
                break;
            case ATTACK:
                addToTop(new ChannelAction(new Lightning()));
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        ((CompanionVigorPower) getPower(CompanionVigorPower.POWER_ID)).frenzyTrigger();
                    addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.LIGHTNING));
                }
                if (hasPower(RoboArmPower.POWER_ID)) {
                    for (int i = 0; i < getPower(RoboArmPower.POWER_ID).amount; i++) {
                        addToTop(new ChannelAction(new Lightning()));
                        if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                            addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.LIGHTNING));
                        }
                    }
                }
                break;
            case PROTECT:
                addToTop(new ChannelAction(new Frost()));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                if (hasPower(RoboArmPower.POWER_ID)) {
                    for (int i = 0; i < getPower(RoboArmPower.POWER_ID).amount; i++) {
                        addToTop(new ChannelAction(new Frost()));
                        addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                    }
                }
                break;
            case SPECIAL:
                if (hasPower(RoboArmPower.POWER_ID)) {
                    int amount = getPower(RoboArmPower.POWER_ID).amount;
                    for (int i = 0; i < amount; i++) {
                        addToTop(new ApplyPowerAction(this, this, new RoboArmPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT));
                    }
                }
                addToTop(new ApplyPowerAction(this, this, new RoboArmPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT));
                break;
        }
    }

    @Override
    public void callDefault() {
        setMove(MOVES[0], DEFAULT, Intent.UNKNOWN);
    }

    @Override
    public void callAttack() {
        getTarget();
        if (hasPower(RoboArmPower.POWER_ID)) {
            setMove(MOVES[1], ATTACK, Intent.ATTACK_BUFF, this.damage.get(0).base, getPower(RoboArmPower.POWER_ID).amount + 1, true, true);
        } else {
            setMove(MOVES[1], ATTACK, Intent.ATTACK_BUFF, this.damage.get(0).base, true);
        }
    }

    @Override
    public void callProtect() {
        if (hasPower(RoboArmPower.POWER_ID)) {
            setMove(MOVES[2], PROTECT, Intent.DEFEND_BUFF, this.block.get(0).base, getPower(RoboArmPower.POWER_ID).amount + 1, true, false);
        } else {
            setMove(MOVES[2], PROTECT, Intent.DEFEND_BUFF, this.block.get(0).base, false);
        }
    }

    @Override
    public void callSpecial() {
        setMove(MOVES[3], SPECIAL, Intent.BUFF);
    }

    @Override
    public void updateIntentTip() {
        switch (nextMove) {
            case DEFAULT:
                this.intentTip.header = MOVES[0];
                this.intentTip.body = INTENTS[0];
                this.intentTip.img = getIntentImg();
                return;
            case ATTACK:
                this.intentTip.header = MOVES[1];
                if (hasPower(RoboArmPower.POWER_ID))
                    this.intentTip.body = INTENTS[1] + this.intentDmg + INTENTS[2] + INTENTS[3] + (getPower(RoboArmPower.POWER_ID).amount + 1) + INTENTS[4] + INTENTS[5] + (getPower(RoboArmPower.POWER_ID).amount + 1) + INTENTS[7];
                else
                    this.intentTip.body = INTENTS[1] + this.intentDmg + INTENTS[5] + 1 + INTENTS[6];
                this.intentTip.img = getIntentImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                if (hasPower(RoboArmPower.POWER_ID))
                    this.intentTip.body = INTENTS[8] + this.intentBlk + INTENTS[9] + INTENTS[10] + (getPower(RoboArmPower.POWER_ID).amount + 1) + INTENTS[11] + INTENTS[12] + (getPower(RoboArmPower.POWER_ID).amount + 1) + INTENTS[14];
                else
                    this.intentTip.body = INTENTS[8] + this.intentBlk + INTENTS[12] + 1 + INTENTS[13];
                this.intentTip.img = getIntentImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                if (hasPower(RoboArmPower.POWER_ID))
                    this.intentTip.body = INTENTS[15] + (getPower(RoboArmPower.POWER_ID).amount + SPECIAL_PWR_AMT) + INTENTS[16];
                else
                    this.intentTip.body = INTENTS[15] + SPECIAL_PWR_AMT + INTENTS[16];
                this.intentTip.img = getIntentImg();
                return;
            case UNKNOWN:
                this.intentTip.header = MOVES[4];
                this.intentTip.body = INTENTS[17];
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
                    if (hasPower(RoboArmPower.POWER_ID))
                        return INTENT_TOOLTIPS[0] + this.damage.get(0).output + INTENT_TOOLTIPS[1] + INTENT_TOOLTIPS[2] + (getPower(RoboArmPower.POWER_ID).amount + 1) + INTENT_TOOLTIPS[3] + INTENT_TOOLTIPS[4] + (getPower(RoboArmPower.POWER_ID).amount + 1) + INTENT_TOOLTIPS[5];
                    else
                        return INTENT_TOOLTIPS[0] + this.damage.get(0).output + INTENT_TOOLTIPS[4] + 1 + INTENT_TOOLTIPS[5];
                }
            case PROTECT:
                if (head) {
                    return MOVES[2];
                } else {
                    if (hasPower(RoboArmPower.POWER_ID))
                        return INTENT_TOOLTIPS[6] + this.block.get(0).output + INTENT_TOOLTIPS[7] + INTENT_TOOLTIPS[8] + (getPower(RoboArmPower.POWER_ID).amount + 1) + INTENT_TOOLTIPS[9] + INTENT_TOOLTIPS[10] + (getPower(RoboArmPower.POWER_ID).amount + 1) + INTENT_TOOLTIPS[11];
                    else
                        return INTENT_TOOLTIPS[3] + this.block.get(0).output + INTENT_TOOLTIPS[10] + 1 + INTENT_TOOLTIPS[11];
                }
            case SPECIAL:
                if (head) {
                    return MOVES[3];
                } else {
                    if (hasPower(RoboArmPower.POWER_ID))
                        return INTENT_TOOLTIPS[12] + (getPower(RoboArmPower.POWER_ID).amount + SPECIAL_PWR_AMT) + INTENT_TOOLTIPS[13];
                    else
                        return INTENT_TOOLTIPS[12] + SPECIAL_PWR_AMT + INTENT_TOOLTIPS[13];
                }
        }
        return "";
    }

    public void useTheCard(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        return;
    }
}
