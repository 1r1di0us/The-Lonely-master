package lonelymod.companions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import lonelymod.actions.CallDefaultAction;
import lonelymod.powers.CompanionDexterityPower;
import lonelymod.powers.CompanionStaminaPower;
import lonelymod.powers.CompanionVigorPower;
import lonelymod.powers.OutcastPower;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

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

    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new OutcastPower(this)));
    }

    public void takeTurn() {
        switch (nextMove) {
            case DEFAULT:
                addToBot(new SFXAction("VO_GREMLINFAT_1C"));
                break;
            case ATTACK:
                addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                if (consecutiveAttack == 3) {
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                else {
                    consecutiveAttack++;
                }
                if (consecutiveProtect != 3)
                    consecutiveProtect = 0;
                if (consecutiveSpecial != 3)
                    consecutiveSpecial = 0;
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, intentBlock));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                if (consecutiveProtect == 3) {
                    addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new ThornsPower(AbstractDungeon.player, EMP_PROTECT_PWR_AMT)));
                } else {
                    consecutiveProtect++;
                }
                if (consecutiveAttack != 3)
                    consecutiveAttack = 0;
                if (consecutiveSpecial != 3)
                    consecutiveSpecial = 0;
                break;
            case SPECIAL:
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT));
                addToBot(new ApplyPowerAction(this, this, new CompanionDexterityPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT));
                if (consecutiveSpecial == 3) {
                    consecutiveProtect = 3;
                    consecutiveAttack = 3;
                } else {
                    consecutiveSpecial++;
                }
                if (consecutiveAttack != 3)
                    consecutiveAttack = 0;
                if (consecutiveProtect != 3)
                    consecutiveProtect = 0;
                break;
        }
        if (this.hasPower(makeID("OutcastPower"))) {
            ((OutcastPower) this.getPower(makeID("OutcastPower"))).updateDescription(consecutiveAttack, consecutiveProtect, consecutiveSpecial);
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
