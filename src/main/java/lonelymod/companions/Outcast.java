package lonelymod.companions;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.CallDefaultAction;

import static lonelymod.LonelyMod.makeCompanionPath;

public class Outcast extends AbstractCompanion {
    public static final String ID = "Outcast";

    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Outcast");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

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
        super(NAME, ID, 0.0F, 0.0F, 90.0F, 120.0F, IMG, drawX, drawY);
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.damage.add(new DamageInfo(this, this.attackDmg));
    }

    public void usePreBattleAction() {}

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
        setMove(MOVES[0], DEFAULT, Intent.UNKNOWN);
        createIntent();
    }

    public void callAttack() {
        flashIntent();
        if (consecutiveAttack == 3) {
            setMove(MOVES[3], ATTACK, Intent.ATTACK, this.damage.get(0).base, EMP_ATTACK_AMT, true);
        } else {
            setMove(MOVES[3], ATTACK, Intent.ATTACK, this.damage.get(0).base);
        }
        createIntent();
    }

    public void callProtect() {
        flashIntent();
        if (consecutiveProtect == 3) {
            setMove(MOVES[2], PROTECT, Intent.DEFEND_BUFF);
        } else {
            setMove(MOVES[2], PROTECT, Intent.DEFEND);
        }
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
                this.intentTip.header = TEXT[50];
                this.intentTip.body = TEXT[51];
                this.intentTip.img = getIntentImg();
                return;
            case ATTACK:
                this.intentTip.header = TEXT[52];
                if (consecutiveAttack == 3) {
                    this.intentTip.body = TEXT[53] + this.intentDmg + TEXT[55] + this.intentMultiAmt + TEXT[56];
                } else {
                    this.intentTip.body = TEXT[53] + this.intentDmg + TEXT[54];
                }
                this.intentTip.img = getIntentImg();
                return;
            case PROTECT:
                this.intentTip.header = TEXT[57];
                if (consecutiveProtect == 3) {
                    this.intentTip.body = TEXT[58] + this.intentBlock + TEXT[60] + EMP_PROTECT_PWR_AMT + TEXT[61];
                } else {
                    this.intentTip.body = TEXT[58] + this.intentBlock + TEXT[59];
                }
                this.intentTip.img = getIntentImg();
                return;
            case SPECIAL:
                this.intentTip.header = TEXT[62];
                if (consecutiveSpecial == 3) {
                    this.intentTip.body = TEXT[63] + SPECIAL_PWR_AMT + TEXT[64] + SPECIAL_PWR_AMT + TEXT[66];
                } else {
                    this.intentTip.body = TEXT[63] + SPECIAL_PWR_AMT + TEXT[64] + SPECIAL_PWR_AMT + TEXT[65];
                }
                this.intentTip.img = getIntentImg();
                return;
        }
        this.intentTip.header = "NOT SET";
        this.intentTip.body = "NOT SET";
        this.intentTip.img = ImageMaster.INTENT_UNKNOWN;
    }
}
