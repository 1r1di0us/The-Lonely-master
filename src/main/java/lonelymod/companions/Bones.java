package lonelymod.companions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import lonelymod.actions.CallDefaultAction;
import lonelymod.powers.BonesPower;
import lonelymod.powers.CompanionVigorPower;
import lonelymod.powers.TargetPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Bones extends AbstractCompanion {
    public static final String ID = "Bones";

    private static final Logger logger = LogManager.getLogger(AbstractMonster.class.getName());

    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Bones");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String IMG = makeCompanionPath("BonesSmall.png");

    private static final int DEFAULT_BLK = 3;
    private static final int ATTACK_DMG = 8;
    private static final int ATTACK_PWR_AMT = 1;
    private static final int PROTECT_BLK = 6;
    private static final int PROTECT_PWR_AMT = 5;
    private static final int SPECIAL_DEBUFF_AMT = 3;
    private static final int SPECIAL_PWR_AMT = 3;

    private int defaultBlk;
    private int attackDmg;
    private int protectBlk;


    public Bones(float drawX, float drawY) {
        super(NAME, ID, 0.0F, 0.0F, 220.0F, 130.0F, IMG, drawX, drawY);
        this.defaultBlk = DEFAULT_BLK;
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.damage.add(new DamageInfo(this, this.attackDmg));
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new BonesPower(this), 1));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case DEFAULT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, intentBlock));
                break;
            case ATTACK: //bug not doing target
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(targetEnemy.hb.cX +

                        MathUtils.random(-25.0F, 25.0F) * Settings.scale, targetEnemy.hb.cY +
                        MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                        .cpy()), 0.0F));
                addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                if (hasPower(CompanionVigorPower.POWER_ID)) {
                    getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                }
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, ATTACK_PWR_AMT), ATTACK_PWR_AMT));
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, intentBlock));
                addToBot(new ApplyPowerAction(this, this, new CompanionVigorPower(this, PROTECT_PWR_AMT), PROTECT_PWR_AMT));
                break;
            case SPECIAL:
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    addToBot(new ApplyPowerAction(mo, this, new VulnerablePower(mo, SPECIAL_DEBUFF_AMT, true), SPECIAL_DEBUFF_AMT, true, AbstractGameAction.AttackEffect.NONE));
                    addToBot(new ApplyPowerAction(mo, this, new TargetPower(mo, SPECIAL_DEBUFF_AMT, true), SPECIAL_DEBUFF_AMT, true, AbstractGameAction.AttackEffect.NONE));
                }
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT, true, AbstractGameAction.AttackEffect.NONE));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(this, SPECIAL_PWR_AMT), SPECIAL_PWR_AMT, true, AbstractGameAction.AttackEffect.NONE));
                break;
        }
        addToBot(new CallDefaultAction());
    }

    @Override
    public void callDefault() {
        setMove(MOVES[0], DEFAULT, AbstractMonster.Intent.DEFEND);
        this.intentBaseBlock = defaultBlk;
        applyPowersToBlock();
        createIntent();
    }

    @Override
    public void callAttack() {
        flashIntent();
        setMove(MOVES[1], ATTACK, AbstractMonster.Intent.ATTACK_BUFF, this.damage.get(0).base);
        this.targetEnemy = getTarget();
        createIntent();
    }

    @Override
    public void callProtect() {
        flashIntent();
        setMove(MOVES[2], PROTECT, AbstractMonster.Intent.DEFEND_BUFF);
        this.intentBaseBlock = protectBlk;
        applyPowersToBlock();
        createIntent();
    }

    @Override
    public void callSpecial() {
        flashIntent();
        setMove(MOVES[3], SPECIAL, AbstractMonster.Intent.STRONG_DEBUFF);
        createIntent();
    }

    @Override
    public void updateIntentTip() {
        switch (nextMove) {
            case DEFAULT:
                this.intentTip.header = TEXT[0];
                this.intentTip.body = TEXT[1] + this.intentBlock + TEXT[2];
                this.intentTip.img = ImageMaster.INTENT_DEFEND_L;
                return;
            case ATTACK:
                this.intentTip.header = TEXT[3];
                this.intentTip.body = TEXT[4] + this.intentDmg + TEXT[5] + ATTACK_PWR_AMT + TEXT[6];
                this.intentTip.img = ImageMaster.INTENT_ATTACK_BUFF;
                return;
            case PROTECT:
                this.intentTip.header = TEXT[7];
                this.intentTip.body = TEXT[8] + this.intentBlock + TEXT[9] + PROTECT_PWR_AMT + TEXT[10];
                this.intentTip.img = ImageMaster.INTENT_DEFEND_BUFF_L;
                return;
            case SPECIAL:
                this.intentTip.header = TEXT[11];
                this.intentTip.body = TEXT[12] + SPECIAL_DEBUFF_AMT + TEXT[13] + SPECIAL_DEBUFF_AMT + TEXT[14] + SPECIAL_PWR_AMT + TEXT[15] + SPECIAL_PWR_AMT + TEXT[16];
                this.intentTip.img = ImageMaster.INTENT_DEBUFF2_L;
                return;
        }
        this.intentTip.header = "NOT SET";
        this.intentTip.body = "NOT SET";
        this.intentTip.img = ImageMaster.INTENT_UNKNOWN;
    }
}
