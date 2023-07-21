package lonelymod.companions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import lonelymod.actions.CallMoveAction;
import lonelymod.powers.CompanionStaminaPower;
import lonelymod.powers.CompanionVigorPower;
import lonelymod.powers.OmenPower;
import lonelymod.powers.TargetPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;


public class Omen extends AbstractCompanion {
    public static final String ID = makeID("Omen");
    public static final String IMG = makeCompanionPath("OmenSmall.png");

    private static final Logger logger = LogManager.getLogger(Omen.class.getName());

    private static final int INIT_PASSIVE_AMT = 5;
    private static final int DEFAULT_DMG = 10;
    private static final int ATTACK_DMG = 1;
    private static final int PROTECT_BLK = 5;
    private static final int PROTECT_AMT = 2;
    private static final int PROTECT_DEBUFF_AMT = 5;
    private static final int SPECIAL_STR_AMT = 1;
    private static final int SPECIAL_PWR_AMT = 5;

    private int defaultDmg;
    private int attackDmg;
    private int protectBlk;

    public Omen(float drawX, float drawY) {
        super("Omen", ID, 0.0F, 0.0F, 190.0F, 251.0F, IMG, drawX, drawY);
        this.defaultDmg = DEFAULT_DMG;
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.damage.add(new DamageInfo(this, this.defaultDmg));
        this.damage.add(new DamageInfo(this, this.attackDmg));
        this.block.add(new BlockInfo(this, this.protectBlk));
    }

    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new OmenPower(this, INIT_PASSIVE_AMT), INIT_PASSIVE_AMT));
        addToBot(new CallMoveAction(PROTECT, this));
    }

    public void takeTurn() {
        switch (nextMove) {
            case DEFAULT:
                addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                if (hasPower(CompanionVigorPower.POWER_ID))
                    getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                break;
            case ATTACK:
                if (this.hasPower(OmenPower.POWER_ID)) {
                    for (int i = 0; i < this.getPower(OmenPower.POWER_ID).amount; i++) {
                        addToBot(new DamageAction(targetEnemy, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    }
                } else {
                    logger.info("ERROR: OMEN SUMMONED WITHOUT POWER");
                    break;
                }
                if (hasPower(CompanionVigorPower.POWER_ID))
                    getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                addToBot(new ApplyPowerAction(targetEnemy, this, new TargetPower(targetEnemy, PROTECT_DEBUFF_AMT, true), PROTECT_DEBUFF_AMT));
                break;
            case SPECIAL:
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, SPECIAL_STR_AMT)));
                addToBot(new ApplyPowerAction(this, this, new OmenPower(this, SPECIAL_PWR_AMT)));
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
                addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                if (hasPower(CompanionVigorPower.POWER_ID))
                    getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                break;
            case ATTACK:
                if (this.hasPower(OmenPower.POWER_ID)) {
                    for (int i = 0; i < this.getPower(OmenPower.POWER_ID).amount; i++) {
                        addToTop(new DamageAction(targetEnemy, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    }
                } else {
                    logger.info("ERROR: OMEN SUMMONED WITHOUT POWER");
                    break;
                }
                if (hasPower(CompanionVigorPower.POWER_ID))
                    getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                break;
            case PROTECT:
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                addToTop(new ApplyPowerAction(targetEnemy, this, new TargetPower(targetEnemy, PROTECT_DEBUFF_AMT, true), PROTECT_DEBUFF_AMT));
                break;
            case SPECIAL:
                addToTop(new ApplyPowerAction(this, this, new StrengthPower(this, SPECIAL_STR_AMT)));
                addToTop(new ApplyPowerAction(this, this, new OmenPower(this, SPECIAL_PWR_AMT)));
                break;
        }
    }

    public void callDefault() {
        getTarget();
        setMove(MOVES[0], DEFAULT, Intent.ATTACK, this.damage.get(0).base, true);
    }

    public void callAttack() {
        getTarget();
        if (this.hasPower(makeID("OmenPower"))) {
            setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(1).base, this.getPower(OmenPower.POWER_ID).amount, true, true);
        } else {
            logger.info("ERROR: OMEN SUMMONED WITHOUT POWER");
            return;
        }
    }

    public void callProtect() {
        getTarget();
        setMove(MOVES[2], PROTECT, Intent.DEFEND_DEBUFF, this.block.get(0).base, false);
    }

    public void callSpecial() {
        setMove(MOVES[3], SPECIAL, Intent.BUFF);
    }

    public void updateIntentTip() {
        switch (nextMove) {
            case DEFAULT:
                this.intentTip.header = MOVES[0];
                this.intentTip.body = INTENTS[0] + this.intentDmg + INTENTS[1];
                this.intentTip.img = getIntentImg();
                return;
            case ATTACK:
                this.intentTip.header = MOVES[1];
                this.intentTip.body = INTENTS[2] + this.intentDmg + INTENTS[3] + this.intentMultiAmt + INTENTS[4];
                this.intentTip.img = getIntentImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                this.intentTip.body = INTENTS[5] + this.intentBlk + INTENTS[6] + PROTECT_AMT + INTENTS[7] + PROTECT_DEBUFF_AMT + INTENTS[8];
                this.intentTip.img = getIntentImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                this.intentTip.body = INTENTS[9] + SPECIAL_STR_AMT + INTENTS[10] + SPECIAL_PWR_AMT + INTENTS[11];
                this.intentTip.img = getIntentImg();
                return;
            case UNKNOWN:
                this.intentTip.header = MOVES[4];
                this.intentTip.body = INTENTS[12];
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
                    return INTENT_TOOLTIPS[0] + this.damage.get(1).output + INTENT_TOOLTIPS[1] + this.getPower(OmenPower.POWER_ID).amount + INTENT_TOOLTIPS[2];
                }
            case PROTECT:
                if (head) {
                    return MOVES[2];
                } else {
                    return INTENT_TOOLTIPS[3] + this.block.get(0).output + INTENT_TOOLTIPS[4] + PROTECT_AMT + INTENT_TOOLTIPS[5] + PROTECT_DEBUFF_AMT + INTENT_TOOLTIPS[6];
                }
            case SPECIAL:
                if (head) {
                    return MOVES[3];
                } else {
                    return INTENT_TOOLTIPS[7] + SPECIAL_STR_AMT + INTENT_TOOLTIPS[8] + SPECIAL_PWR_AMT + INTENT_TOOLTIPS[9];
                }
        }
        return "";
    }

    public void useTheCard(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        return;
    }
}
