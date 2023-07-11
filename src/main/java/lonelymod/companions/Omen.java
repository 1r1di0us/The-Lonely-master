package lonelymod.companions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import lonelymod.actions.CallDefaultAction;
import lonelymod.actions.SummonBonesAction;
import lonelymod.powers.OmenPower;
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
    }

    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new OmenPower(this, INIT_PASSIVE_AMT), INIT_PASSIVE_AMT));
    }

    public void takeTurn() {
        switch (nextMove) {
            case DEFAULT:
                break;
            case ATTACK:
                break;
            case PROTECT:
                break;
            case SPECIAL:
                break;
        }
        addToBot(new CallDefaultAction());
    }

    public void callDefault() {
        setMove(MOVES[0], DEFAULT, Intent.ATTACK, this.damage.get(0).base);
        this.targetEnemy = getTarget();
        createIntent();
    }

    public void callAttack() {
        flashIntent();
        if (this.hasPower(makeID("OmenPower"))) {
            setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(1).base, this.getPower(makeID("OmenPower")).amount, true);
        } else {
            logger.info("ERROR: OMEN SUMMONED WITHOUT POWER");
            return;
        }
        this.targetEnemy = getTarget();
        createIntent();
    }

    public void callProtect() {
        flashIntent();
        setMove(MOVES[2], PROTECT, Intent.DEFEND_DEBUFF);
        this.intentBaseBlock = protectBlk;
        applyPowersToBlock();
        this.targetEnemy = getTarget();
        createIntent();
    }

    public void callSpecial() {
        flashIntent();
        setMove(MOVES[3], SPECIAL, Intent.BUFF);
        createIntent();
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
                this.intentTip.body = TEXT[42] + this.intentBlock + TEXT[43] + PROTECT_AMT + TEXT[44] + PROTECT_DEBUFF_AMT + TEXT[45];
                this.intentTip.img = getIntentImg();
                return;
            case SPECIAL:
                this.intentTip.header = TEXT[46];
                this.intentTip.body = TEXT[47] + SPECIAL_STR_AMT + TEXT[48] + SPECIAL_PWR_AMT + TEXT[49];
                this.intentTip.img = getIntentImg();
                return;
        }
        this.intentTip.header = "NOT SET";
        this.intentTip.body = "NOT SET";
        this.intentTip.img = ImageMaster.INTENT_UNKNOWN;
    }
}
