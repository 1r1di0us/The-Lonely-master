package lonelymod.companions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.powers.StrengthPower;
import lonelymod.powers.CompanionStaminaPower;
import lonelymod.powers.CompanionVigorPower;
import lonelymod.powers.MechanicPower;
import lonelymod.powers.RoboArmPower;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Mechanic extends AbstractCompanion {
    public static final String ID = makeID("Mechanic");
    public static final String IMG = makeCompanionPath("Mechanic.png");

    private static final int ATTACK_DMG = 6;
    private static final int PROTECT_BLK = 5;
    private static final int SPECIAL_PWR_AMT = 1;
    private static final int SPECIAL_DMG_AMT = 5;

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
        addToBot(new ApplyPowerAction(this, this, new MechanicPower(this)));
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
                addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.LIGHTNING));
                if (hasPower(CompanionVigorPower.POWER_ID))
                    getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                addToBot(new ChannelAction(new Lightning()));
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                addToBot(new ChannelAction(new Frost()));
                break;
            case SPECIAL:
                addToBot(new ApplyPowerAction(this, this, new RoboArmPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT));
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
                int roll = MathUtils.random(1);
                if (roll == 0) {
                    addToBot(new SFXAction("VO_GREMLINDOPEY_1A"));
                } else {
                    addToBot(new SFXAction("VO_GREMLINDOPEY_1B"));
                }
                break;
            case ATTACK:
                addToTop(new ChannelAction(new Lightning()));
                addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.LIGHTNING));
                if (hasPower(CompanionVigorPower.POWER_ID))
                    getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                break;
            case PROTECT:
                addToTop(new ChannelAction(new Frost()));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case SPECIAL:
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
        setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, true);
    }

    @Override
    public void callProtect() {
        setMove(MOVES[2], PROTECT, Intent.DEFEND_BUFF, this.block.get(0).base, false);
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
                this.intentTip.body = INTENTS[1] + this.intentDmg + INTENTS[2];
                this.intentTip.img = getIntentImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                this.intentTip.body = INTENTS[3] + this.intentBlk + INTENTS[4];
                this.intentTip.img = getIntentImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                this.intentTip.body = INTENTS[5] + SPECIAL_DMG_AMT + INTENTS[6];
                this.intentTip.img = getIntentImg();
                return;
            case UNKNOWN:
                this.intentTip.header = MOVES[4];
                this.intentTip.body = INTENTS[7];
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
                    return INTENT_TOOLTIPS[0] + this.damage.get(0).output + INTENT_TOOLTIPS[1];
                }
            case PROTECT:
                if (head) {
                    return MOVES[2];
                } else {
                    return INTENT_TOOLTIPS[2] + this.block.get(0).output + INTENT_TOOLTIPS[3];
                }
            case SPECIAL:
                if (head) {
                    return MOVES[3];
                } else {
                    return INTENT_TOOLTIPS[4] + SPECIAL_DMG_AMT + INTENT_TOOLTIPS[5];
                }
        }
        return "";
    }
}
