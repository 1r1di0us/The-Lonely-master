package lonelymod.companions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import lonelymod.cards.summonmoves.*;
import lonelymod.fields.CompanionField;
import lonelymod.powers.*;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Spy extends AbstractCompanion {
    public static final String ID = makeID("Spy");
    public static final String IMG = makeCompanionPath("Spy.png");

    private static final int ATTACK_DMG = 6;
    private static final int PROTECT_BLK = 6;
    private static final int PROTECT_PWR_AMT = 1;
    private static final int SPECIAL_SHIV_AMT = 3;
    private static final int SPECIAL_ENVENOM_AMT = 2;
    private static final int SPECIAL_ATTACK_AMT = 1;

    private int attackDmg;
    private int protectBlk;

    public Spy() {
        super("Dzil", ID, 0.0F, 0.0F, 80.0F, 100.0F, IMG);
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.damage.add(new DamageInfo(this, this.attackDmg, DamageInfo.DamageType.THORNS));
        this.block.add(new BlockInfo(this, this.protectBlk));

        this.cardToPreview.addAll(CardTips);
    }

    public static final ArrayList<AbstractCard> CardTips = new ArrayList<AbstractCard>() {
        {
            add(new SpyNothing());
            add(new Assassinate());
            add(new Speedy());
            add(new Venomous());
        }
    };

    @Override
    public void useOnSummonAction(boolean onBattleStart) {
        CompanionField.playableCards.set(AbstractDungeon.player, new ArrayList<>());
        CompanionField.playableCards.get(AbstractDungeon.player).add(new Shiv());
        addToTop(new ApplyPowerAction(this, this, new SpyPower(this, 1)));
    }

    @Override
    public void performTurn() {
        switch (this.nextMove) {
            case DEFAULT:
                talk();
                int roll = MathUtils.random(1);
                if (roll == 0) {
                    addToBot(new SFXAction("VO_GREMLINSPAZZY_1A"));
                } else {
                    addToBot(new SFXAction("VO_GREMLINSPAZZY_1B"));
                }
                break;
            case ATTACK:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (hasPower(SpyPower.POWER_ID)) {
                        for (int i = 0; i < getPower(SpyPower.POWER_ID).amount; i++) {
                            addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                        }
                    }
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NextTurnBlockPower(AbstractDungeon.player, this.block.get(0).output)));
                addToBot(new ApplyPowerAction(this, this, new CompanionDexterityPower(this, PROTECT_PWR_AMT)));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case SPECIAL:
                addToBot(new ApplyPowerAction(this, this, new SpyPower(this, SPECIAL_SHIV_AMT)));
                addToBot(new ApplyPowerAction(this, this, new CompanionEnvenomPower(this, SPECIAL_ENVENOM_AMT)));
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
                    addToBot(new SFXAction("VO_GREMLINSPAZZY_1A"));
                } else {
                    addToBot(new SFXAction("VO_GREMLINSPAZZY_1B"));
                }
                break;
            case ATTACK:
                getTarget();
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        ((CompanionVigorPower) getPower(CompanionVigorPower.POWER_ID)).instantTrigger();
                    if (hasPower(SpyPower.POWER_ID)) {
                        for (int i = 0; i < getPower(SpyPower.POWER_ID).amount; i++) {
                            addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                        }
                    }
                }
                break;
            case PROTECT:
                if (hasPower(StaminaPower.POWER_ID))
                    ((StaminaPower) getPower(StaminaPower.POWER_ID)).instantTrigger();
                addToTop(new ApplyPowerAction(this, this, new CompanionDexterityPower(this, PROTECT_PWR_AMT)));
                addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NextTurnBlockPower(AbstractDungeon.player, this.block.get(0).output)));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                break;
            case SPECIAL:
                addToTop(new ApplyPowerAction(this, this, new AttackNextTurnPower(this, SPECIAL_ATTACK_AMT)));
                addToTop(new ApplyPowerAction(this, this, new CompanionEnvenomPower(this, SPECIAL_ENVENOM_AMT)));
                addToTop(new ApplyPowerAction(this, this, new SpyPower(this, SPECIAL_SHIV_AMT)));
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
                if (hasPower(SpyPower.POWER_ID) && getPower(SpyPower.POWER_ID).amount > 1)
                    setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, getPower(SpyPower.POWER_ID).amount, true, true);
                else
                    setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, true);
                break;
            case PROTECT:
                setMove(MOVES[2], PROTECT, Intent.DEFEND_BUFF, this.block.get(0).base, false);
                break;
            case SPECIAL:
                if (allowRetarget) getTarget();
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
                if (this.hasPower(SpyPower.POWER_ID) && this.getPower(SpyPower.POWER_ID).amount > 1) {
                    this.intentTip.body = INTENTS[1] + this.intentDmg + INTENTS[3] + this.getPower(SpyPower.POWER_ID).amount + INTENTS[4];
                } else if (this.hasPower(SpyPower.POWER_ID) && this.getPower(SpyPower.POWER_ID).amount == 1) {
                    this.intentTip.body = INTENTS[1] + this.intentDmg + INTENTS[2];
                } else {
                    //set up incorrectly
                    this.intentTip.body = "";
                }
                this.intentTip.img = getIntentTipImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                this.intentTip.body = INTENTS[5] + this.intentBlk + INTENTS[6] + this.intentBlk + INTENTS[7] + PROTECT_PWR_AMT + INTENTS[8];
                this.intentTip.img = getIntentTipImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                this.intentTip.body = INTENTS[9] + SPECIAL_SHIV_AMT + INTENTS[10] + SPECIAL_ENVENOM_AMT + INTENTS[11];
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
                    if (this.hasPower(SpyPower.POWER_ID) && this.getPower(SpyPower.POWER_ID).amount > 1) {
                        return INTENT_TOOLTIPS[0] + this.intentDmg + INTENT_TOOLTIPS[2] + this.getPower(SpyPower.POWER_ID).amount + INTENT_TOOLTIPS[3];
                    } else if (this.hasPower(SpyPower.POWER_ID) && this.getPower(SpyPower.POWER_ID).amount == 1) {
                        return INTENT_TOOLTIPS[0] + this.intentDmg + INTENT_TOOLTIPS[1];
                    } else {
                        //if set up incorrectly
                        return "";
                    }
                }
            case PROTECT:
                if (head) {
                    return MOVES[2];
                } else {
                    return INTENT_TOOLTIPS[4] + this.block.get(0).output + INTENT_TOOLTIPS[5] + this.block.get(0).output + INTENT_TOOLTIPS[6] + PROTECT_PWR_AMT + INTENT_TOOLTIPS[7];
                }
            case SPECIAL:
                if (head) {
                    return MOVES[3];
                } else {
                    return INTENT_TOOLTIPS[8] + SPECIAL_SHIV_AMT + INTENT_TOOLTIPS[9] + SPECIAL_ENVENOM_AMT + INTENT_TOOLTIPS[10];
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
        if (card instanceof Shiv) {
            addToBot(new ApplyPowerAction(this, this, new SpyPower(this, 1)));
            if (nextMove == ATTACK) {
                callMove(ATTACK, false, true, false);
            }
        }
    }
}
