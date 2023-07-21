package lonelymod.companions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import lonelymod.powers.*;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Outcast extends AbstractCompanion {
    public static final String ID = makeID("Outcast");
    public static final String IMG = makeCompanionPath("OutcastSmall.png");

    private static final int ATTACK_DMG = 6;
    private static final int EMP_ATTACK_AMT = 3;
    private static final int PROTECT_BLK = 5;
    private static final int EMP_PROTECT_PWR_AMT = 3;
    private static final int SPECIAL_PWR_AMT = 2;

    private int attackDmg;
    private int protectBlk;
    private int consecutiveAttack = 0, consecutiveProtect = 0, consecutiveSpecial = 0;

    public Outcast(float drawX, float drawY) {
        super("Fring", ID, 0.0F, 0.0F, 90.0F, 120.0F, IMG, drawX, drawY);
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.damage.add(new DamageInfo(this, this.attackDmg));
        this.block.add(new BlockInfo(this, this.protectBlk));
    }

    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new OutcastPower(this)));
    }

    public void takeTurn() {
        switch (nextMove) {
            case DEFAULT:
                int roll = MathUtils.random(2);
                if (roll == 0) {
                    addToBot(new SFXAction("VO_GREMLINFAT_1A"));
                } else if (roll == 1) {
                    addToBot(new SFXAction("VO_GREMLINFAT_1B"));
                } else {
                    addToBot(new SFXAction("VO_GREMLINFAT_1C"));
                }
                break;
            case ATTACK:
                addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                if (consecutiveAttack == 3) {
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                else {
                    consecutiveAttack++;
                }
                if (consecutiveProtect != 3)
                    consecutiveProtect = 0;
                if (consecutiveSpecial != 3)
                    consecutiveSpecial = 0;
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                if (consecutiveProtect == 3) {
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new ThornsPower(AbstractDungeon.player, EMP_PROTECT_PWR_AMT)));
                } else {
                    consecutiveProtect++;
                }
                if (consecutiveAttack != 3)
                    consecutiveAttack = 0;
                if (consecutiveSpecial != 3)
                    consecutiveSpecial = 0;
                break;
            case SPECIAL:
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT));
                addToBot(new ApplyPowerAction(this, this, new CompanionDexterityPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT));
                if (consecutiveSpecial == 3) {
                    consecutiveProtect = 3;
                    consecutiveAttack = 3;
                } else {
                    consecutiveSpecial++;
                }
                if (consecutiveAttack != 3)
                    consecutiveAttack = 0;
                if (consecutiveProtect != 3)
                    consecutiveProtect = 0;
                break;
            case UNKNOWN:
                break;
            case NONE:
                break;
        }
        if (this.hasPower(makeID("OutcastPower")))
            ((OutcastPower) this.getPower(makeID("OutcastPower"))).updateDescription(consecutiveAttack, consecutiveProtect, consecutiveSpecial);
    }

    public void performTurn(byte move) {
        switch (move) {
            case DEFAULT:
                int roll = MathUtils.random(2);
                if (roll == 0) {
                    addToBot(new SFXAction("VO_GREMLINFAT_1A"));
                } else if (roll == 1) {
                    addToBot(new SFXAction("VO_GREMLINFAT_1B"));
                } else {
                    addToBot(new SFXAction("VO_GREMLINFAT_1C"));
                }
                break;
            case ATTACK:
                addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                if (consecutiveAttack == 3) {
                    addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                else {
                    consecutiveAttack++;
                }
                if (consecutiveProtect != 3)
                    consecutiveProtect = 0;
                if (consecutiveSpecial != 3)
                    consecutiveSpecial = 0;
                break;
            case PROTECT:
                if (consecutiveProtect == 3) {
                    addToTop(new ApplyPowerAction(AbstractDungeon.player, this, new ThornsPower(AbstractDungeon.player, EMP_PROTECT_PWR_AMT)));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                } else {
                    consecutiveProtect++;
                }
                if (consecutiveAttack != 3)
                    consecutiveAttack = 0;
                if (consecutiveSpecial != 3)
                    consecutiveSpecial = 0;
                break;
            case SPECIAL:
                addToTop(new ApplyPowerAction(this, this, new CompanionDexterityPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT));
                addToTop(new ApplyPowerAction(this, this, new StrengthPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT));
                if (consecutiveSpecial == 3) {
                    consecutiveProtect = 3;
                    consecutiveAttack = 3;
                } else {
                    consecutiveSpecial++;
                }
                if (consecutiveAttack != 3)
                    consecutiveAttack = 0;
                if (consecutiveProtect != 3)
                    consecutiveProtect = 0;
                break;
        }
        if (this.hasPower(makeID("OutcastPower")))
            ((OutcastPower) this.getPower(makeID("OutcastPower"))).updateDescription(consecutiveAttack, consecutiveProtect, consecutiveSpecial);
    }

    public void callDefault() {
        setMove(MOVES[0], DEFAULT, Intent.UNKNOWN);
    }

    public void callAttack() {
        getTarget();
        if (consecutiveAttack == 3) {
            setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, EMP_ATTACK_AMT, true, true);
        } else {
            setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, true);
        }
    }

    public void callProtect() {
        if (consecutiveProtect == 3) {
            setMove(MOVES[2], PROTECT, Intent.DEFEND_BUFF, this.block.get(0).base, false);
        } else {
            setMove(MOVES[2], PROTECT, Intent.DEFEND, this.block.get(0).base, false);
        }
    }

    public void callSpecial() {
        setMove(MOVES[3], SPECIAL, Intent.BUFF);
    }

    public void updateIntentTip() {
        switch (nextMove) {
            case DEFAULT:
                this.intentTip.header = MOVES[0];
                this.intentTip.body = INTENTS[0];
                this.intentTip.img = getIntentImg();
                return;
            case ATTACK:
                this.intentTip.header = MOVES[1];
                if (consecutiveAttack == 3) {
                    this.intentTip.body = INTENTS[1] + this.intentDmg + INTENTS[3] + this.intentMultiAmt + INTENTS[4];
                } else {
                    this.intentTip.body = INTENTS[1] + this.intentDmg + INTENTS[2];
                }
                this.intentTip.img = getIntentImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                if (consecutiveProtect == 3) {
                    this.intentTip.body = INTENTS[5] + this.intentBlk + INTENTS[7] + EMP_PROTECT_PWR_AMT + INTENTS[8];
                } else {
                    this.intentTip.body = INTENTS[5] + this.intentBlk + INTENTS[6];
                }
                this.intentTip.img = getIntentImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                if (consecutiveSpecial == 3) {
                    this.intentTip.body = INTENTS[9] + SPECIAL_PWR_AMT + INTENTS[10] + SPECIAL_PWR_AMT + INTENTS[12];
                } else {
                    this.intentTip.body = INTENTS[9] + SPECIAL_PWR_AMT + INTENTS[10] + SPECIAL_PWR_AMT + INTENTS[11];
                }
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
                    if (consecutiveAttack != 3) return INTENT_TOOLTIPS[0] + this.damage.get(0).output + INTENT_TOOLTIPS[1];
                    else return INTENT_TOOLTIPS[2] + this.damage.get(0).output + INTENT_TOOLTIPS[3] + EMP_ATTACK_AMT + INTENT_TOOLTIPS[4];
                }
            case PROTECT:
                if (head) {
                    return MOVES[2];
                } else {
                    if (consecutiveProtect != 3) return INTENT_TOOLTIPS[5] + this.block.get(0).output + INTENT_TOOLTIPS[6];
                    else return INTENT_TOOLTIPS[7] + this.block.get(0).output + INTENT_TOOLTIPS[8] + EMP_PROTECT_PWR_AMT + INTENT_TOOLTIPS[9];
                }
            case SPECIAL:
                if (head) {
                    return MOVES[3];
                } else {
                    if (consecutiveSpecial != 3) return INTENT_TOOLTIPS[10] + SPECIAL_PWR_AMT + INTENT_TOOLTIPS[11] + SPECIAL_PWR_AMT + INTENT_TOOLTIPS[12];
                    else return INTENT_TOOLTIPS[13] + SPECIAL_PWR_AMT + INTENT_TOOLTIPS[14] + SPECIAL_PWR_AMT + INTENT_TOOLTIPS[15];
                }
        }
        return "";
    }

    public void useTheCard(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        return;
    }
}
