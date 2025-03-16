package lonelymod.companions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import lonelymod.cards.summonmoves.*;
import lonelymod.powers.CompanionVigorPower;
import lonelymod.powers.ManiacPower;
import lonelymod.powers.StaminaPower;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Maniac extends AbstractCompanion {
    public static final String ID = makeID("Maniac");
    public static final String IMG = makeCompanionPath("Maniac.png");

    private static final int ATTACK_DMG = 5;
    private static final int ATTACK_AMT = 2;
    private static final int PROTECT_BLK = 8;
    private static final int PROTECT_DEBUFF_AMT = 2;
    private static final int SPECIAL_PWR_AMT = 5;

    private int attackDmg;
    private int protectBlk;

    public Maniac() {
        super("Kharn", ID, 0.0F, 0.0F, 90.0F, 140.0F, IMG);
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.damage.add(new DamageInfo(this, this.attackDmg, DamageInfo.DamageType.THORNS));
        this.block.add(new BlockInfo(this, this.protectBlk));

        this.cardToPreview.addAll(CardTips);
    }

    public static final ArrayList<AbstractCard> CardTips = new ArrayList<AbstractCard>() {
        {
            add(new ManiacNothing());
            add(new Harass());
            add(new Scare());
            add(new Sacrifice());
        }
    };

    @Override
    public void usePreBattleAction() {
        addToTop(new ApplyPowerAction(this, this, new ManiacPower(this)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case DEFAULT:
                talk();
                int roll = MathUtils.random(2);
                if (roll == 0) {
                    addToBot(new SFXAction("VO_GREMLINANGRY_1A"));
                } else if (roll == 1) {
                    addToBot(new SFXAction("VO_GREMLINANGRY_1B"));
                } else {
                    addToBot(new SFXAction("VO_GREMLINANGRY_1C"));
                }
                break;
            case ATTACK:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToBot(new ApplyPowerAction(targetEnemy, this, new VulnerablePower(targetEnemy, PROTECT_DEBUFF_AMT, true)));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case SPECIAL:
                if (this.hasPower(StrengthPower.POWER_ID)) {
                    addToBot(new RemoveSpecificPowerAction(this, this, getPower(StrengthPower.POWER_ID)));
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(this, (this.getPower(StrengthPower.POWER_ID).amount + SPECIAL_PWR_AMT))));
                } else {
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(this, SPECIAL_PWR_AMT)));
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
                    addToBot(new SFXAction("VO_GREMLINANGRY_1A"));
                } else if (roll == 1) {
                    addToBot(new SFXAction("VO_GREMLINANGRY_1B"));
                } else {
                    addToBot(new SFXAction("VO_GREMLINANGRY_1C"));
                }
                break;
            case ATTACK:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                break;
            case PROTECT:
                addToTop(new ApplyPowerAction(targetEnemy, this, new VulnerablePower(targetEnemy, PROTECT_DEBUFF_AMT, true)));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case SPECIAL:
                if (this.hasPower(StrengthPower.POWER_ID))
                    addToTop(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(this, (this.getPower(StrengthPower.POWER_ID).amount + SPECIAL_PWR_AMT))));
                else
                    addToTop(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(this, SPECIAL_PWR_AMT)));
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
        setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, ATTACK_AMT, true, true);
    }

    @Override
    public void callProtect() {
        getTarget();
        setMove(MOVES[2], PROTECT, Intent.DEFEND_DEBUFF, this.block.get(0).base, false);
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
                this.intentTip.body = INTENTS[1] + this.intentDmg + INTENTS[2] + ATTACK_AMT + INTENTS[3];
                this.intentTip.img = getIntentImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                this.intentTip.body = INTENTS[4] + this.intentBlk + INTENTS[5] + PROTECT_DEBUFF_AMT + INTENTS[6];
                this.intentTip.img = getIntentImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                this.intentTip.body = INTENTS[7] + SPECIAL_PWR_AMT + INTENTS[8];
                this.intentTip.img = getIntentImg();
                return;
            case UNKNOWN:
                this.intentTip.header = MOVES[4];
                this.intentTip.body = INTENTS[9];
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
                    return INTENT_TOOLTIPS[0] + this.damage.get(0).output + INTENT_TOOLTIPS[1] + ATTACK_AMT + INTENT_TOOLTIPS[2];
                }
            case PROTECT:
                if (head) {
                    return MOVES[2];
                } else {
                    return INTENT_TOOLTIPS[3] + this.block.get(0).output + INTENT_TOOLTIPS[4] + PROTECT_DEBUFF_AMT + INTENT_TOOLTIPS[5];
                }
            case SPECIAL:
                if (head) {
                    return MOVES[3];
                } else {
                    return INTENT_TOOLTIPS[6] + SPECIAL_PWR_AMT + INTENT_TOOLTIPS[7];
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
