package lonelymod.companions;

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
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import lonelymod.cards.summonmoves.*;
import lonelymod.fields.CompanionField;
import lonelymod.powers.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Omen extends AbstractCompanion {
    public static final String ID = makeID("Omen");
    public static final String IMG = makeCompanionPath("Omen.png");

    private static final Logger logger = LogManager.getLogger(Omen.class.getName());

    private static final int INIT_CLAWS_AMT = 5;
    private static final int DEFAULT_DMG = 10;
    private static final int ATTACK_DMG = 2;
    private static final int PROTECT_BLK = 6;
    private static final int PROTECT_AMT = 2;
    private static final int PROTECT_DEBUFF_AMT = 3;
    private static final int SPECIAL_ATK_AMT = 3;
    private static final int SPECIAL_PWR_AMT = 1;

    private int defaultDmg;
    private int attackDmg;
    private int protectBlk;

    public Omen() {
        super("Omen", ID, 0.0F, 0.0F, 190.0F, 251.0F, IMG);
        this.defaultDmg = DEFAULT_DMG;
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.damage.add(new DamageInfo(this, this.defaultDmg, DamageInfo.DamageType.THORNS));
        this.damage.add(new DamageInfo(this, this.attackDmg, DamageInfo.DamageType.THORNS));
        this.block.add(new BlockInfo(this, this.protectBlk));

        this.cardToPreview.addAll(CardTips);
    }

    public static final ArrayList<AbstractCard> CardTips = new ArrayList<AbstractCard>() {
        {
            add(new Peck());
            add(new Shred());
            add(new Shriek());
            add(new Sharpen());
        }
    };

    public void useOnSummonAction(boolean onBattleStart) {
        CompanionField.playableCards.set(AbstractDungeon.player, null);
        addToTop(new ApplyPowerAction(this, this, new OmenPower(this, INIT_CLAWS_AMT)));
    }

    public void performTurn() {
        switch (nextMove) {
            case DEFAULT:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                break;
            case ATTACK:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
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
                    if (hasPower(OmenPower.POWER_ID))
                        getPower(OmenPower.POWER_ID).onSpecificTrigger();
                }
                break;
            case PROTECT:
                talk();
                addToBot(new SFXAction("ATTACK_PIERCING_WAIL"));
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped())
                    addToBot(new ApplyPowerAction(targetEnemy, this, new TargetPower(targetEnemy, PROTECT_DEBUFF_AMT, true), PROTECT_DEBUFF_AMT));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case SPECIAL:
                addToBot(new ApplyPowerAction(this, this, new RitualPower(this, SPECIAL_PWR_AMT, false)));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new AttackNextTurnPower(AbstractDungeon.player, SPECIAL_ATK_AMT)));
                break;
        }
    }

    public void performImmediately(byte move) {
        switch (move) {
            case DEFAULT:
                getTarget();
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        ((CompanionVigorPower) getPower(CompanionVigorPower.POWER_ID)).instantTrigger();
                    addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                break;
            case ATTACK:
                getTarget();
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (hasPower(OmenPower.POWER_ID))
                        getPower(OmenPower.POWER_ID).onSpecificTrigger();
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        ((CompanionVigorPower) getPower(CompanionVigorPower.POWER_ID)).instantTrigger();
                    if (this.hasPower(OmenPower.POWER_ID)) {
                        for (int i = 0; i < this.getPower(OmenPower.POWER_ID).amount; i++) {
                            addToTop(new DamageAction(targetEnemy, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                        }
                    } else {
                        logger.info("ERROR: OMEN SUMMONED WITHOUT POWER");
                        break;
                    }
                }
                break;
            case PROTECT:
                getTarget();
                if (hasPower(StaminaPower.POWER_ID))
                    ((StaminaPower) getPower(StaminaPower.POWER_ID)).instantTrigger();
                talk();
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped())
                    addToTop(new ApplyPowerAction(targetEnemy, this, new TargetPower(targetEnemy, PROTECT_DEBUFF_AMT, true), PROTECT_DEBUFF_AMT));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToTop(new SFXAction("ATTACK_PIERCING_WAIL"));
                break;
            case SPECIAL:
                addToTop(new ApplyPowerAction(AbstractDungeon.player, this, new AttackNextTurnPower(AbstractDungeon.player, SPECIAL_ATK_AMT)));
                addToTop(new ApplyPowerAction(this, this, new RitualPower(this, SPECIAL_PWR_AMT, false)));
                break;
        }
    }

    public void setupMove(byte move, boolean allowRetarget) {
        switch (move) {
            case DEFAULT:
                if (allowRetarget) getTarget();
                setMove(MOVES[0], DEFAULT, Intent.ATTACK, this.damage.get(0).base, true);
                break;
            case ATTACK:
                if (allowRetarget) getTarget();
                if (this.hasPower(OmenPower.POWER_ID)) {
                    setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(1).base, this.getPower(OmenPower.POWER_ID).amount, true, true);
                } else {
                    logger.info("ERROR: OMEN SUMMONED WITHOUT POWER");
                }
                break;
            case PROTECT:
                if (allowRetarget) getTarget();
                setMove(MOVES[2], PROTECT, Intent.DEFEND_DEBUFF, this.block.get(0).base, PROTECT_AMT, true, false);
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
                this.intentTip.body = INTENTS[0] + this.intentDmg + INTENTS[1];
                this.intentTip.img = getIntentTipImg();
                return;
            case ATTACK:
                this.intentTip.header = MOVES[1];
                if (hasPower(OmenPower.POWER_ID))
                    this.intentTip.body = INTENTS[2] + this.intentDmg + INTENTS[3] + this.getPower(OmenPower.POWER_ID).amount + INTENTS[4];
                else
                    this.intentTip.body = INTENTS[2] + this.intentDmg + INTENTS[3] + INIT_CLAWS_AMT + INTENTS[4];
                this.intentTip.img = getIntentTipImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                this.intentTip.body = INTENTS[5] + this.intentBlk + INTENTS[6] + PROTECT_AMT + INTENTS[7] + PROTECT_DEBUFF_AMT + INTENTS[8];
                this.intentTip.img = getIntentTipImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                this.intentTip.body = INTENTS[9] + SPECIAL_PWR_AMT + INTENTS[10] + SPECIAL_ATK_AMT + INTENTS[11];
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
                    if (hasPower(OmenPower.POWER_ID))
                        return INTENT_TOOLTIPS[0] + this.damage.get(1).output + INTENT_TOOLTIPS[1] + this.getPower(OmenPower.POWER_ID).amount + INTENT_TOOLTIPS[2];
                    else
                        return INTENT_TOOLTIPS[0] + this.damage.get(1).output + INTENT_TOOLTIPS[1] + INIT_CLAWS_AMT + INTENT_TOOLTIPS[2];
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
                    return INTENT_TOOLTIPS[7] + SPECIAL_PWR_AMT + INTENT_TOOLTIPS[8] + SPECIAL_ATK_AMT + INTENT_TOOLTIPS[9];
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
