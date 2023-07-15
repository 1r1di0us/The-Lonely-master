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
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import lonelymod.actions.CallMoveAction;
import lonelymod.powers.CompanionStaminaPower;
import lonelymod.powers.CompanionVigorPower;
import lonelymod.powers.MeatPower;

import static lonelymod.LonelyMod.makeCompanionPath;

public class Meat extends AbstractCompanion {
    public static final String ID = "Meat";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Meat");
    public static final String NAME = monsterStrings.NAME;

    public static final String[] MOVES = monsterStrings.MOVES;

    public static final String IMG = makeCompanionPath("Meat.png");


    private static final int DEFAULT_PWR_AMT = 3;
    private static final int ATTACK_DMG = 6, ATTACK_AMT = 2, ATTACK_EMP_AMT = 3;
    private static final int PROTECT_BLK = 6, PROTECT_AMT = 3;
    private static final int PROTECT_PWR_AMT = 5;
    private static final int SPECIAL_DMG = 10;
    private static final int SPECIAL_DEBUFF_AMT = 3;
    private int attackDmg;
    private int protectBlk;
    private int specialDmg;

    public Meat(float drawX, float drawY) {
        super(NAME, ID, 0.0F, 0.0F, 400.0F, 300.0F, IMG, drawX, drawY);
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.specialDmg = SPECIAL_DMG;
        this.damage.add(new DamageInfo(this, this.attackDmg));
        this.damage.add(new DamageInfo(this, this.specialDmg));
        this.block.add(new BlockInfo(this, this.protectBlk));
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new MeatPower(this), 1));
    }

    @Override
    public void takeTurn(boolean callDefault) {
        switch (this.nextMove) {
            case DEFAULT:
                addToBot(new ApplyPowerAction(this, this, new CompanionVigorPower(this, DEFAULT_PWR_AMT), DEFAULT_PWR_AMT));
                addToBot(new ApplyPowerAction(this, this, new CompanionStaminaPower(this, DEFAULT_PWR_AMT), DEFAULT_PWR_AMT));
                break;
            case ATTACK:
                addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                if (targetEnemy.hasPower(ConstrictedPower.POWER_ID))
                    addToBot(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                if (hasPower(CompanionVigorPower.POWER_ID))
                    getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                break;
            case PROTECT:
                addToBot(new GainBlockAction(AbstractDungeon.player, this, intentBlk));
                addToBot(new GainBlockAction(AbstractDungeon.player, this, intentBlk));
                addToBot(new GainBlockAction(AbstractDungeon.player, this, intentBlk));
                if (hasPower(CompanionStaminaPower.POWER_ID))
                    getPower(CompanionStaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case SPECIAL:
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(targetEnemy.hb.cX +
                        MathUtils.random(-25.0F, 25.0F) * Settings.scale, targetEnemy.hb.cY +
                        MathUtils.random(-25.0F, 25.0F) * Settings.scale, Color.GOLD
                        .cpy()), 0.0F));
                addToBot(new DamageAction(targetEnemy, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                if (hasPower(CompanionVigorPower.POWER_ID))
                    getPower(CompanionVigorPower.POWER_ID).onSpecificTrigger();
                if (targetEnemy.hasPower(WeakPower.POWER_ID))
                    addToBot(new ApplyPowerAction(targetEnemy, this, new ConstrictedPower(targetEnemy, this, SPECIAL_DEBUFF_AMT), SPECIAL_DEBUFF_AMT));
                break;
            case NONE:
                break;
        }
        if (callDefault)
            addToBot(new CallMoveAction(DEFAULT));
    }

    @Override
    public void callDefault() {
        setMove(MOVES[0], DEFAULT, Intent.BUFF);
        createIntent();
    }

    @Override
    public void callAttack() {
        flashIntent();
        this.targetEnemy = getTarget();
        if (targetEnemy.hasPower(ConstrictedPower.POWER_ID))
            setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, ATTACK_EMP_AMT, true, true);
        else
            setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, ATTACK_AMT, true, true);
        createIntent();
    }

    @Override
    public void callProtect() {
        flashIntent();
        setMove(MOVES[2], PROTECT, Intent.DEFEND, this.block.get(0).base, PROTECT_AMT, true, false);
        createIntent();
    }

    @Override
    public void callSpecial() {
        flashIntent();
        setMove(MOVES[3], SPECIAL, Intent.ATTACK_DEBUFF, this.damage.get(1).base, true);
        this.targetEnemy = getTarget();
        createIntent();
    }

    @Override
    public void updateIntentTip() {
        switch (nextMove) {
            case DEFAULT:
                this.intentTip.header = TEXT[17];
                this.intentTip.body = TEXT[18] + DEFAULT_PWR_AMT + TEXT[19] + DEFAULT_PWR_AMT + TEXT[20];
                this.intentTip.img = getIntentImg();
                return;
            case ATTACK:
                this.intentTip.header = TEXT[21];
                this.intentTip.body = TEXT[22] + this.intentDmg + TEXT[23] + ATTACK_AMT + TEXT[24] + 1 + TEXT[25];
                this.intentTip.img = getIntentImg();
                return;
            case PROTECT:
                this.intentTip.header = TEXT[26];
                this.intentTip.body = TEXT[27] + this.intentBlk + TEXT[28] + PROTECT_AMT + TEXT[29];
                this.intentTip.img = getIntentImg();
                return;
            case SPECIAL:
                this.intentTip.header = TEXT[30];
                this.intentTip.body = TEXT[31] + this.intentDmg + TEXT[32] + SPECIAL_DEBUFF_AMT + TEXT[33];
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
