package lonelymod.companions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import lonelymod.cards.summonmoves.*;
import lonelymod.fields.CompanionField;
import lonelymod.powers.*;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Outcast extends AbstractCompanion {
    public static final String ID = makeID("Outcast");
    public static final String IMG = makeCompanionPath("Outcast.png");

    private static final int ATTACK_DMG = 10;
    private static final int EMP_ATTACK_AMT = 2;
    private static final int PROTECT_BLK = 8;
    private static final int SPECIAL_TRIGGER_AMT = 2;
    private static final int EMP_SPECIAL_STR_INC = 1;
    private static final int EMP_SPECIAL_DEX_INC = 1;
    private static final int EMPOWER_STR = 2;
    private static final int EMPOWER_DEX = 2;

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

    public void useOnSummonAction(boolean onBattleStart) {
        CompanionField.playableCards.set(AbstractDungeon.player, null);
        addToTop(new ApplyPowerAction(this, this, new OutcastPower(this, 0, true, EMPOWER_STR, EMPOWER_DEX)));
    }

    public void performTurn() {
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
                    if (hasPower(EmpoweredPower.POWER_ID)) {
                        for (int i = 0; i < EMP_ATTACK_AMT; i++)
                            addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                        addToTop(new ReducePowerAction(this, this, getPower(EmpoweredPower.POWER_ID), 1));
                    } else
                        addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                break;
            case PROTECT:
                if (hasPower(EmpoweredPower.POWER_ID)) {
                    addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                    addToBot(new ApplyPowerAction(this, this, new CompanionVigorPower(this, this.block.get(0).output)));
                    addToTop(new ReducePowerAction(this, this, getPower(EmpoweredPower.POWER_ID), 1));
                }
                else
                    addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case SPECIAL:
                if (hasPower(OutcastPower.POWER_ID)) {
                    if (hasPower(EmpoweredPower.POWER_ID)) {
                        ((OutcastPower) getPower(OutcastPower.POWER_ID)).upgradeEmpower(EMP_SPECIAL_STR_INC, EMP_SPECIAL_DEX_INC);
                        addToTop(new ReducePowerAction(this, this, getPower(EmpoweredPower.POWER_ID), 1));
                    }
                    for (int i = 0; i < SPECIAL_TRIGGER_AMT; i++)
                        getPower(OutcastPower.POWER_ID).onSpecificTrigger();
                }
                break;
            case UNKNOWN:
                break;
            case NONE:
                break;
        }
    }

    public void performImmediately(byte move) {
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
                getTarget();
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        ((CompanionVigorPower) getPower(CompanionVigorPower.POWER_ID)).instantTrigger();
                    if (hasPower(EmpoweredPower.POWER_ID)) {
                        for (int i = 0; i < EMP_ATTACK_AMT; i++)
                            addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                        addToTop(new ReducePowerAction(this, this, getPower(EmpoweredPower.POWER_ID), 1));
                    } else
                        addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                break;
            case PROTECT:
                if (hasPower(StaminaPower.POWER_ID))
                    ((StaminaPower) getPower(StaminaPower.POWER_ID)).instantTrigger();
                if (hasPower(EmpoweredPower.POWER_ID)) {
                    addToTop(new ApplyPowerAction(this, this, new CompanionVigorPower(this, this.block.get(0).output)));
                    addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                    addToTop(new ReducePowerAction(this, this, getPower(EmpoweredPower.POWER_ID), 1));
                }
                else
                    addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                break;
            case SPECIAL:
                if (hasPower(OutcastPower.POWER_ID)) {
                    if (hasPower(EmpoweredPower.POWER_ID)) {
                        ((OutcastPower) getPower(OutcastPower.POWER_ID)).upgradeEmpower(EMP_SPECIAL_STR_INC, EMP_SPECIAL_DEX_INC);
                        addToTop(new ReducePowerAction(this, this, getPower(EmpoweredPower.POWER_ID), 1));
                    }
                    for (int i = 0; i < SPECIAL_TRIGGER_AMT; i++)
                        getPower(OutcastPower.POWER_ID).onSpecificTrigger();
                }
                break;
        }
    }

    public void setupMove(byte move, boolean allowRetarget) {
        switch (move) {
            case DEFAULT:
                setMove(MOVES[0], DEFAULT, Intent.UNKNOWN);
                break;
            case ATTACK:
                if (allowRetarget) getTarget();
                if (hasPower(EmpoweredPower.POWER_ID)) {
                    setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, EMP_ATTACK_AMT, true, true);
                } else {
                    setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, true);
                }
                break;
            case PROTECT:
                if (hasPower(EmpoweredPower.POWER_ID)) {
                    setMove(MOVES[2], PROTECT, Intent.DEFEND_BUFF, this.block.get(0).base, false);
                } else {
                    setMove(MOVES[2], PROTECT, Intent.DEFEND, this.block.get(0).base, false);
                }
                break;
            case SPECIAL:
                setMove(MOVES[3], SPECIAL, Intent.BUFF);
                break;
        }

    }

    public void updateIntentTip() {
        switch (nextMove) {
            case DEFAULT:
                this.intentTip.header = MOVES[0];
                this.intentTip.body = INTENTS[0];
                this.intentTip.img = getIntentTipImg();
                return;
            case ATTACK:
                this.intentTip.header = MOVES[1];
                if (hasPower(EmpoweredPower.POWER_ID)) {
                    this.intentTip.body = INTENTS[1] + this.intentDmg + INTENTS[3] + this.intentMultiAmt + INTENTS[4];
                } else {
                    this.intentTip.body = INTENTS[1] + this.intentDmg + INTENTS[2];
                }
                this.intentTip.img = getIntentTipImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                if (hasPower(EmpoweredPower.POWER_ID)) {
                    this.intentTip.body = INTENTS[5] + this.intentBlk + INTENTS[7];
                } else {
                    this.intentTip.body = INTENTS[5] + this.intentBlk + INTENTS[6];
                }
                this.intentTip.img = getIntentTipImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                if (hasPower(EmpoweredPower.POWER_ID)) {
                    this.intentTip.body = INTENTS[8] + EMP_SPECIAL_STR_INC + INTENTS[9] + SPECIAL_TRIGGER_AMT + INTENTS[11];
                } else {
                    this.intentTip.body = INTENTS[10] + SPECIAL_TRIGGER_AMT + INTENTS[11];
                }
                this.intentTip.img = getIntentTipImg();
                return;
            case UNKNOWN:
                this.intentTip.header = MOVES[4];
                this.intentTip.body = INTENTS[12];
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
                    if (hasPower(EmpoweredPower.POWER_ID))
                        return INTENT_TOOLTIPS[2] + this.damage.get(0).output + INTENT_TOOLTIPS[3] + EMP_ATTACK_AMT + INTENT_TOOLTIPS[4];
                    else return INTENT_TOOLTIPS[0] + this.damage.get(0).output + INTENT_TOOLTIPS[1];
                }
            case PROTECT:
                if (head) {
                    return MOVES[2];
                } else {
                    if (hasPower(EmpoweredPower.POWER_ID))
                        return INTENT_TOOLTIPS[7] + this.block.get(0).output + INTENT_TOOLTIPS[8];
                    else return INTENT_TOOLTIPS[5] + this.block.get(0).output + INTENT_TOOLTIPS[6];
                }
            case SPECIAL:
                if (head) {
                    return MOVES[3];
                } else {
                    if (hasPower(EmpoweredPower.POWER_ID))
                        return INTENT_TOOLTIPS[11] + EMP_SPECIAL_STR_INC + INTENT_TOOLTIPS[12] + SPECIAL_TRIGGER_AMT + INTENT_TOOLTIPS[13];
                    else return INTENT_TOOLTIPS[9] + SPECIAL_TRIGGER_AMT + INTENT_TOOLTIPS[10];
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
