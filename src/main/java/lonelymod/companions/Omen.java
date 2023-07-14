package lonelymod.companions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import lonelymod.actions.CallDefaultAction;
import lonelymod.actions.CallProtectAction;
import lonelymod.actions.SummonBonesAction;
import lonelymod.powers.CompanionStaminaPower;
import lonelymod.powers.CompanionVigorPower;
import lonelymod.powers.OmenPower;
import lonelymod.powers.TargetPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;


public class Omen extends AbstractCompanion {
    public static final String ID = "Omen";

    private static final Logger logger = LogManager.getLogger(SummonBonesAction.class.getName());

    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Omen");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String IMG = makeCompanionPath("OmenSmall.png");

    private static final int INIT_PASSIVE_AMT = 5;
    private static final int DEFAULT_DMG = 10;
    private static final int ATTACK_DMG = 1;
    private static final int PROTECT_BLK = 5;
    private static final int PROTECT_AMT = 2;
    private static final int PROTECT_DEBUFF_AMT = 5;
    private static final int SPECIAL_STR_AMT = 2;
    private static final int SPECIAL_PWR_AMT = 5;

    private int defaultDmg;
    private int attackDmg;
    private int protectBlk;

    public Omen(float drawX, float drawY) {
        super(NAME, ID, 0.0F, 0.0F, 190.0F, 251.0F, IMG, drawX, drawY);
        this.defaultDmg = DEFAULT_DMG;
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.damage.add(new DamageInfo(this, this.defaultDmg));
        this.damage.add(new DamageInfo(this, this.attackDmg));
        this.block.add(new BlockInfo(this, this.protectBlk));
    }

    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new OmenPower(this, INIT_PASSIVE_AMT), INIT_PASSIVE_AMT));
        addToBot(new CallProtectAction());
    }

    public void takeTurn(boolean callDefault) {
        switch (nextMove) {
            case DEFAULT:
                addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                if (hasPower(CompanionVigorPower.POWER_ID))
                    getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                break;
            case ATTACK:
                if (this.hasPower(makeID("OmenPower"))) {
                    for (int i = 0; i < this.getPower(makeID("OmenPower")).amount; i++) {
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
                addToBot(new GainBlockAction(AbstractDungeon.player, this, intentBlk));
                addToBot(new GainBlockAction(AbstractDungeon.player, this, intentBlk));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                addToBot(new ApplyPowerAction(targetEnemy, this, new TargetPower(targetEnemy, PROTECT_DEBUFF_AMT, true), PROTECT_DEBUFF_AMT));
                break;
            case SPECIAL:
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, SPECIAL_STR_AMT)));
                addToBot(new ApplyPowerAction(this, this, new OmenPower(this, SPECIAL_PWR_AMT)));
                break;
            case NONE:
                break;
        }
        if (callDefault)
            addToBot(new CallDefaultAction());
    }

    public void callDefault() {
        setMove(MOVES[0], DEFAULT, Intent.ATTACK, this.damage.get(0).base, true);
        this.targetEnemy = getTarget();
        createIntent();
    }

    public void callAttack() {
        if (nextMove == NONE) {
            AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, TEXT[67], false));
        } else {
            flashIntent();
            if (this.hasPower(makeID("OmenPower"))) {
                setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(1).base, this.getPower(makeID("OmenPower")).amount, true, true);
            } else {
                logger.info("ERROR: OMEN SUMMONED WITHOUT POWER");
                return;
            }
            this.targetEnemy = getTarget();
            createIntent();
        }
    }

    public void callProtect() {
        if (nextMove == NONE) {
            AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, TEXT[67], false));
        } else {
            flashIntent();
            setMove(MOVES[2], PROTECT, Intent.DEFEND_DEBUFF, this.block.get(0).base, false);
            this.targetEnemy = getTarget();
            createIntent();
        }
    }

    public void callSpecial() {
        if (nextMove == NONE) {
            AbstractDungeon.effectList.add(new ThoughtBubble(this.dialogX, this.dialogY, 3.0F, TEXT[67], false));
        } else {
            flashIntent();
            setMove(MOVES[3], SPECIAL, Intent.BUFF);
            createIntent();
        }
    }

    public void updateIntentTip() {
        switch (nextMove) {
            case DEFAULT:
                this.intentTip.header = TEXT[34];
                this.intentTip.body = TEXT[35] + this.intentDmg + TEXT[36];
                this.intentTip.img = getIntentImg();
                return;
            case ATTACK:
                this.intentTip.header = TEXT[37];
                this.intentTip.body = TEXT[38] + this.intentDmg + TEXT[39] + this.intentMultiAmt + TEXT[40];
                this.intentTip.img = getIntentImg();
                return;
            case PROTECT:
                this.intentTip.header = TEXT[41];
                this.intentTip.body = TEXT[42] + this.intentBlk + TEXT[43] + PROTECT_AMT + TEXT[44] + PROTECT_DEBUFF_AMT + TEXT[45];
                this.intentTip.img = getIntentImg();
                return;
            case SPECIAL:
                this.intentTip.header = TEXT[46];
                this.intentTip.body = TEXT[47] + SPECIAL_STR_AMT + TEXT[48] + SPECIAL_PWR_AMT + TEXT[49];
                this.intentTip.img = getIntentImg();
                return;
            case NONE:
                this.intentTip.header = "";
                this.intentTip.body = "";
                this.intentTip.img = ImageMaster.INTENT_UNKNOWN;
                return;
        }
        this.intentTip.header = "NOT SET";
        this.intentTip.body = "NOT SET";
        this.intentTip.img = ImageMaster.INTENT_UNKNOWN;
    }
}
