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
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import lonelymod.cards.summonmoves.*;
import lonelymod.powers.OutcastPower;
import lonelymod.powers.CompanionVigorPower;
import lonelymod.powers.CompanionDexterityPower;
import lonelymod.powers.StaminaPower;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Outcast extends AbstractCompanion {
    public static final String ID = makeID("Outcast");
    public static final String IMG = makeCompanionPath("OutcastSmall.png");

    private static final int ATTACK_DMG = 10;
    private static final int EMP_ATTACK_AMT = 2;
    private static final int PROTECT_BLK = 8;
    private static final int EMP_PROTECT_PWR_AMT = 5;
    private static final int SPECIAL_PWR_AMT = 5;
    private static final int EMP_SPECIAL_PWR_AMT = 3;

    private int attackDmg;
    private int protectBlk;

    public Outcast() {
        super("Fring", ID, 0.0F, 0.0F, 90.0F, 120.0F, IMG);
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.damage.add(new DamageInfo(this, this.attackDmg, DamageInfo.DamageType.THORNS));
        this.block.add(new BlockInfo(this, this.protectBlk));

        this.cardToPreview.addAll(CardTips);
    }

    public static final ArrayList<AbstractCard> CardTips = new ArrayList<AbstractCard>() {
        {
            add(new OutcastNothing());
            add(new SpearJab());
            add(new HoldGround());
            add(new TrainUp());
        }
    };

    public void usePreBattleAction() {
        addToTop(new ApplyPowerAction(this, this, new OutcastPower(this, 0, true)));
    }

    public void takeTurn() {
        switch (nextMove) {
            case DEFAULT:
                talk();
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
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (hasPower(OutcastPower.POWER_ID) && getPower(OutcastPower.POWER_ID).amount == 3) {
                        for (int i = 0; i < EMP_ATTACK_AMT; i++)
                            addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                    } else {
                        addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    }
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(OutcastPower.POWER_ID) && getPower(OutcastPower.POWER_ID).amount == 3)
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new ThornsPower(AbstractDungeon.player, EMP_PROTECT_PWR_AMT)));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case SPECIAL:
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, SPECIAL_PWR_AMT)));
                addToBot(new ApplyPowerAction(this, this, new CompanionDexterityPower(this, SPECIAL_PWR_AMT)));
                if (hasPower(OutcastPower.POWER_ID) && getPower(OutcastPower.POWER_ID).amount == 3) {
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, EMP_SPECIAL_PWR_AMT)));
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, EMP_SPECIAL_PWR_AMT)));
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
                talk();
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
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        ((CompanionVigorPower) getPower(CompanionVigorPower.POWER_ID)).frenzyTrigger();
                    if (hasPower(OutcastPower.POWER_ID) && getPower(OutcastPower.POWER_ID).amount == 3) {
                        for (int i = 0; i < EMP_ATTACK_AMT; i++)
                            addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                    } else {
                        addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    }
                }
                break;
            case PROTECT:
                if (hasPower(OutcastPower.POWER_ID) && getPower(OutcastPower.POWER_ID).amount == 3)
                    addToTop(new ApplyPowerAction(AbstractDungeon.player, this, new ThornsPower(AbstractDungeon.player, EMP_PROTECT_PWR_AMT)));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case SPECIAL:
                if (hasPower(OutcastPower.POWER_ID) && getPower(OutcastPower.POWER_ID).amount == 3) {
                    addToTop(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, EMP_SPECIAL_PWR_AMT)));
                    addToTop(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, EMP_SPECIAL_PWR_AMT)));
                }
                addToTop(new ApplyPowerAction(this, this, new CompanionDexterityPower(this, SPECIAL_PWR_AMT)));
                addToTop(new ApplyPowerAction(this, this, new StrengthPower(this, SPECIAL_PWR_AMT)));
                break;
        }
    }

    public void callDefault() {
        setMove(MOVES[0], DEFAULT, Intent.UNKNOWN);
    }

    public void callAttack() {
        getTarget();
        /*consecutiveMove++;
        if (consecutiveMove == 4) {
            if (hasPower(OutcastPower.POWER_ID)) {
                getPower(OutcastPower.POWER_ID).onSpecificTrigger();
            }
            consecutiveMove = 1;
        } else {
            addToBot(new ApplyPowerAction(this, this, new OutcastPower(this, 1, false)));
        }*/
        if (hasPower(OutcastPower.POWER_ID) && getPower(OutcastPower.POWER_ID).amount == 2) {
            setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, EMP_ATTACK_AMT, true, true);
        } else {
            setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, true);
        }
    }

    public void callProtect() {
        /*consecutiveMove++;
        if (consecutiveMove == 4) {
            if (hasPower(OutcastPower.POWER_ID)) {
                getPower(OutcastPower.POWER_ID).onSpecificTrigger();
            }
            consecutiveMove = 1;
        } else {
            addToBot(new ApplyPowerAction(this, this, new OutcastPower(this, 1, false)));
        }*/
        if (hasPower(OutcastPower.POWER_ID) && getPower(OutcastPower.POWER_ID).amount == 2) {
            setMove(MOVES[2], PROTECT, Intent.DEFEND_BUFF, this.block.get(0).base, false);
        } else {
            setMove(MOVES[2], PROTECT, Intent.DEFEND, this.block.get(0).base, false);
        }
    }

    public void callSpecial() {
        /*consecutiveMove++;
        if (consecutiveMove == 4) {
            if (hasPower(OutcastPower.POWER_ID)) {
                getPower(OutcastPower.POWER_ID).onSpecificTrigger();
            }
            consecutiveMove = 1;
        } else {
            addToBot(new ApplyPowerAction(this, this, new OutcastPower(this, 1, false)));
        }*/
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
                if (hasPower(OutcastPower.POWER_ID) && getPower(OutcastPower.POWER_ID).amount == 3) {
                    this.intentTip.body = INTENTS[1] + this.intentDmg + INTENTS[3] + this.intentMultiAmt + INTENTS[4];
                } else {
                    this.intentTip.body = INTENTS[1] + this.intentDmg + INTENTS[2];
                }
                this.intentTip.img = getIntentImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                if (hasPower(OutcastPower.POWER_ID) && getPower(OutcastPower.POWER_ID).amount == 3) {
                    this.intentTip.body = INTENTS[5] + this.intentBlk + INTENTS[7] + EMP_PROTECT_PWR_AMT + INTENTS[8];
                } else {
                    this.intentTip.body = INTENTS[5] + this.intentBlk + INTENTS[6];
                }
                this.intentTip.img = getIntentImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                if (hasPower(OutcastPower.POWER_ID) && getPower(OutcastPower.POWER_ID).amount == 3) {
                    this.intentTip.body = INTENTS[9] + SPECIAL_PWR_AMT + INTENTS[10] + SPECIAL_PWR_AMT + INTENTS[12] + EMP_SPECIAL_PWR_AMT + INTENTS[13] + EMP_SPECIAL_PWR_AMT + INTENTS[14];
                } else {
                    this.intentTip.body = INTENTS[9] + SPECIAL_PWR_AMT + INTENTS[10] + SPECIAL_PWR_AMT + INTENTS[11];
                }
                this.intentTip.img = getIntentImg();
                return;
            case UNKNOWN:
                this.intentTip.header = MOVES[4];
                this.intentTip.body = INTENTS[15];
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
                    if (hasPower(OutcastPower.POWER_ID) && getPower(OutcastPower.POWER_ID).amount == 3)
                        return INTENT_TOOLTIPS[2] + this.damage.get(0).output + INTENT_TOOLTIPS[3] + EMP_ATTACK_AMT + INTENT_TOOLTIPS[4];
                    else return INTENT_TOOLTIPS[0] + this.damage.get(0).output + INTENT_TOOLTIPS[1];
                }
            case PROTECT:
                if (head) {
                    return MOVES[2];
                } else {
                    if (hasPower(OutcastPower.POWER_ID) && getPower(OutcastPower.POWER_ID).amount == 3)
                        return INTENT_TOOLTIPS[7] + this.block.get(0).output + INTENT_TOOLTIPS[8] + EMP_PROTECT_PWR_AMT + INTENT_TOOLTIPS[9];
                    else return INTENT_TOOLTIPS[5] + this.block.get(0).output + INTENT_TOOLTIPS[6];
                }
            case SPECIAL:
                if (head) {
                    return MOVES[3];
                } else {
                    if (hasPower(OutcastPower.POWER_ID) && getPower(OutcastPower.POWER_ID).amount == 3)
                        return INTENT_TOOLTIPS[13] + SPECIAL_PWR_AMT + INTENT_TOOLTIPS[14] + SPECIAL_PWR_AMT + INTENT_TOOLTIPS[15] + EMP_SPECIAL_PWR_AMT + INTENT_TOOLTIPS[16] + EMP_SPECIAL_PWR_AMT + INTENT_TOOLTIPS[17];
                    else return INTENT_TOOLTIPS[10] + SPECIAL_PWR_AMT + INTENT_TOOLTIPS[11] + SPECIAL_PWR_AMT + INTENT_TOOLTIPS[12];
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
