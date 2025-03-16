package lonelymod.companions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.unique.MulticastAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import lonelymod.cards.summonmoves.*;
import lonelymod.powers.MechanicPower;
import lonelymod.powers.CompanionVigorPower;
import lonelymod.powers.StaminaPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.ArrayList;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Mechanic extends AbstractCompanion {
    public static final String ID = makeID("Mechanic");
    public static final String IMG = makeCompanionPath("Mechanic.png");

    private static final Logger logger = LogManager.getLogger(Mechanic.class.getName());
    private static final int ATTACK_DMG = 8;
    private static final int PROTECT_BLK = 8;
    private static final int SPECIAL_ORB_AMT = 1;

    private int attackDmg;
    private int protectBlk;

    public Mechanic() {
        super("Blirt", ID, 0.0F, 0.0F, 100.0F, 120.0F, IMG);
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.damage.add(new DamageInfo(this, this.attackDmg, DamageInfo.DamageType.THORNS));
        this.block.add(new BlockInfo(this, this.protectBlk));

        this.cardToPreview.addAll(CardTips);
    }

    public static final ArrayList<AbstractCard> CardTips = new ArrayList<AbstractCard>() {
        {
            add(new MechanicNothing());
            add(new Electrocute());
            add(new IceWall());
            add(new Supercritical());
        }
    };

    @Override
    public void usePreBattleAction() {
        addToTop(new ApplyPowerAction(this, this, new MechanicPower(this), -1));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case DEFAULT:
                talk();
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
                if (hasPower(MechanicPower.POWER_ID)) {
                    for (int i = 0; i < getPower(MechanicPower.POWER_ID).amount; i++) {
                        addToBot(new ChannelAction(new Lightning()));
                    }
                }
                else
                    logger.info("ERROR: MECHANIC SUMMONED WITHOUT POWER");
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                if (hasPower(MechanicPower.POWER_ID)) {
                    for (int i = 0; i < getPower(MechanicPower.POWER_ID).amount; i++) {
                        addToBot(new ChannelAction(new Frost()));
                    }
                }
                else
                    logger.info("ERROR: MECHANIC SUMMONED WITHOUT POWER");
                break;
            case SPECIAL:
                if (hasPower(MechanicPower.POWER_ID))
                    addToBot(new MulticastAction(AbstractDungeon.player, getPower(MechanicPower.POWER_ID).amount, false, true));
                else
                    logger.info("ERROR: MECHANIC SUMMONED WITHOUT POWER");
                addToBot(new ChannelAction(new Plasma()));
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
                int roll = MathUtils.random(1);
                if (roll == 0) {
                    addToBot(new SFXAction("VO_GREMLINDOPEY_1A"));
                } else {
                    addToBot(new SFXAction("VO_GREMLINDOPEY_1B"));
                }
                break;
            case ATTACK:
                if (hasPower(MechanicPower.POWER_ID)) {
                    for (int i = 0; i < getPower(MechanicPower.POWER_ID).amount; i++) {
                        addToTop(new ChannelAction(new Lightning()));
                    }
                }
                else
                    logger.info("ERROR: MECHANIC SUMMONED WITHOUT POWER");
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        ((CompanionVigorPower) getPower(CompanionVigorPower.POWER_ID)).frenzyTrigger();
                    addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.LIGHTNING));
                }
                break;
            case PROTECT:
                if (hasPower(MechanicPower.POWER_ID)) {
                    for (int i = 0; i < getPower(MechanicPower.POWER_ID).amount; i++) {
                        addToTop(new ChannelAction(new Frost()));
                    }
                }
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                else
                    logger.info("ERROR: MECHANIC SUMMONED WITHOUT POWER");
                break;
            case SPECIAL:
                addToTop(new ChannelAction(new Plasma()));
                if (hasPower(MechanicPower.POWER_ID))
                    addToTop(new MulticastAction(AbstractDungeon.player, getPower(MechanicPower.POWER_ID).amount, false, true));
                else
                    logger.info("ERROR: MECHANIC SUMMONED WITHOUT POWER");
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
        setMove(MOVES[1], ATTACK, Intent.ATTACK_BUFF, this.damage.get(0).base, true);
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
                if (hasPower(MechanicPower.POWER_ID))
                    this.intentTip.body = INTENTS[1] + this.intentDmg + INTENTS[2] + getPower(MechanicPower.POWER_ID).amount + INTENTS[3];
                else
                    logger.info("ERROR: MECHANIC SUMMONED WITHOUT POWER");
                this.intentTip.img = getIntentImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                if (hasPower(MechanicPower.POWER_ID))
                    this.intentTip.body = INTENTS[4] + this.intentBlk + INTENTS[5] + getPower(MechanicPower.POWER_ID).amount + INTENTS[6];
                else
                    logger.info("ERROR: MECHANIC SUMMONED WITHOUT POWER");
                this.intentTip.img = getIntentImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                if (hasPower(MechanicPower.POWER_ID))
                    this.intentTip.body = INTENTS[7] + getPower(MechanicPower.POWER_ID).amount + INTENTS[8] + SPECIAL_ORB_AMT + INTENTS[9];
                else
                    logger.info("ERROR: MECHANIC SUMMONED WITHOUT POWER");
                this.intentTip.img = getIntentImg();
                return;
            case UNKNOWN:
                this.intentTip.header = MOVES[4];
                this.intentTip.body = INTENTS[10];
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
                    if (hasPower(MechanicPower.POWER_ID))
                        return INTENT_TOOLTIPS[0] + this.damage.get(0).output + INTENT_TOOLTIPS[1] + getPower(MechanicPower.POWER_ID).amount + INTENT_TOOLTIPS[2];
                    else
                        logger.info("ERROR: MECHANIC SUMMONED WITHOUT POWER");
                }
            case PROTECT:
                if (head) {
                    return MOVES[2];
                } else {
                    if (hasPower(MechanicPower.POWER_ID))
                        return INTENT_TOOLTIPS[3] + this.block.get(0).output + INTENT_TOOLTIPS[4] + getPower(MechanicPower.POWER_ID).amount + INTENT_TOOLTIPS[5];
                    else
                        logger.info("ERROR: MECHANIC SUMMONED WITHOUT POWER");
                }
            case SPECIAL:
                if (head) {
                    return MOVES[3];
                } else {
                    if (hasPower(MechanicPower.POWER_ID))
                        return INTENT_TOOLTIPS[6] + getPower(MechanicPower.POWER_ID).amount + INTENT_TOOLTIPS[7] + SPECIAL_ORB_AMT + INTENT_TOOLTIPS[8];
                    else
                        logger.info("ERROR: MECHANIC SUMMONED WITHOUT POWER");
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
