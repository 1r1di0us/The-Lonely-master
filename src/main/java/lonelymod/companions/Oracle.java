package lonelymod.companions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.tempCards.Insight;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import lonelymod.actions.OraclePowerAction;
import lonelymod.cards.summonmoves.*;
import lonelymod.fields.CompanionField;
import lonelymod.powers.*;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Oracle extends AbstractCompanion {
    public static final String ID = makeID("Oracle");
    public static final String IMG = makeCompanionPath("Oracle.png");

    public static final int INIT_POWER_AMT = 5;
    private static final int ATTACK_DMG = 10;
    private static final int PROTECT_BLK = 6;
    public static final int SPECIAL_MANTRA_AMT = 1;
    public static final int SPECIAL_VIGOR_MOD = 5;
    public static final int SPECIAL_ATTACK_AMT = 1;

    private int attackDmg;
    private int protectBlk;

    public Oracle() {
        super("Sowru", ID, 0.0F, 0.0F, 100.0F, 150.0F, IMG);
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.damage.add(new DamageInfo(this, this.attackDmg, DamageInfo.DamageType.THORNS));
        this.block.add(new BlockInfo(this, this.protectBlk));

        this.cardToPreview.addAll(CardTips);
    }

    public static final ArrayList<AbstractCard> CardTips = new ArrayList<AbstractCard>() {
        {
            add(new OracleNothing());
            add(new FieryBlast());
            add(new PeacefulProtest());
            add(new Blessing());
        }
    };

    @Override
    public void useOnSummonAction(boolean onBattleStart) {
        CompanionField.playableCards.set(AbstractDungeon.player, null);
        if (!onBattleStart) {
            addToTop(new OraclePowerAction(this, INIT_POWER_AMT, INIT_POWER_AMT*2, INIT_POWER_AMT*3)); //start of turn do the thing
        }
        addToTop(new ApplyPowerAction(this, this, new OraclePower(this, INIT_POWER_AMT)));
    }

    @Override
    public void performTurn() {
        switch (this.nextMove) {
            case DEFAULT:
                talk();
                int roll = MathUtils.random(1);
                if (roll == 0) {
                    addToBot(new SFXAction("VO_GREMLINDOPEY_1A"));
                } else {
                    addToBot(new SFXAction("VO_GREMLINDOPEY_1B"));
                }
                break;
            case ATTACK:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToBot(new ChangeStanceAction(CalmStance.STANCE_ID));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case SPECIAL:
                if (hasPower(CompanionVigorPower.POWER_ID)) {
                    addToBot(new ApplyPowerAction(this, this, new GrantMantraNextTurnPower(this, SPECIAL_MANTRA_AMT * Math.floorDiv(getPower(CompanionVigorPower.POWER_ID).amount, SPECIAL_VIGOR_MOD))));
                }
                addToBot(new ApplyPowerAction(this, this, new AttackNextTurnPower(this, SPECIAL_ATTACK_AMT)));
                break;
        }
    }

    public void performImmediately(byte move) {
        switch (move) {
            case DEFAULT:
                talk();
                int roll = MathUtils.random(1);
                if (roll == 0) {
                    addToBot(new SFXAction("VO_GREMLINDOPEY_1A"));
                } else {
                    addToBot(new SFXAction("VO_GREMLINDOPEY_1B"));
                }
                break;
            case ATTACK:
                getTarget();
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        ((CompanionVigorPower) getPower(CompanionVigorPower.POWER_ID)).instantTrigger();
                    addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                }
                break;
            case PROTECT:
                if (hasPower(StaminaPower.POWER_ID))
                    ((StaminaPower) getPower(StaminaPower.POWER_ID)).instantTrigger();
                addToTop(new ChangeStanceAction(CalmStance.STANCE_ID));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                break;
            case SPECIAL:
                addToTop(new ApplyPowerAction(this, this, new AttackNextTurnPower(this, SPECIAL_ATTACK_AMT)));
                if (hasPower(CompanionVigorPower.POWER_ID)) {
                    addToTop(new ApplyPowerAction(this, this, new GrantMantraNextTurnPower(this, SPECIAL_MANTRA_AMT * Math.floorDiv(getPower(CompanionVigorPower.POWER_ID).amount, SPECIAL_VIGOR_MOD))));
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
                setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, true, true);
                break;
            case PROTECT:
                setMove(MOVES[2], PROTECT, Intent.DEFEND_BUFF, this.block.get(0).base, false);
                break;
            case SPECIAL:
                setMove(MOVES[3], SPECIAL, Intent.BUFF);
                break;
        }
    }

    @Override
    public void updateIntentTip() {
        switch (nextMove) {
            case DEFAULT:
                this.intentTip.header = MOVES[0];
                this.intentTip.body = INTENTS[0];
                this.intentTip.img = getIntentTipImg();
                return;
            case ATTACK:
                this.intentTip.header = MOVES[1];
                this.intentTip.body = INTENTS[1] + this.intentDmg + INTENTS[2];
                this.intentTip.img = getIntentTipImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                this.intentTip.body = INTENTS[3] + this.intentBlk + INTENTS[4];
                this.intentTip.img = getIntentTipImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                this.intentTip.body = INTENTS[5] + SPECIAL_MANTRA_AMT + INTENTS[6] + SPECIAL_VIGOR_MOD + INTENTS[7];
                this.intentTip.img = getIntentTipImg();
                return;
            case UNKNOWN:
                this.intentTip.header = MOVES[4];
                this.intentTip.body = INTENTS[8];
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
                    return INTENT_TOOLTIPS[4] + SPECIAL_MANTRA_AMT + INTENT_TOOLTIPS[5] + SPECIAL_VIGOR_MOD + INTENT_TOOLTIPS[6];
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
