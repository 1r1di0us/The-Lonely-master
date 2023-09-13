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
import com.megacrit.cardcrawl.powers.PoisonPower;
import lonelymod.powers.CompanionDexterityPower;
import lonelymod.powers.CompanionVigorPower;
import lonelymod.powers.SpyPower;
import lonelymod.powers.StaminaPower;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Spy extends AbstractCompanion {
    public static final String ID = makeID("Spy");
    public static final String IMG = makeCompanionPath("Spy.png");

    private static final int ATTACK_DMG = 6;
    private static final int PROTECT_BLK = 6;
    private static final int PROTECT_PWR_AMT = 1;
    private static final int SPECIAL_DEBUFF_AMT = 25;

    private int attackDmg;
    private int protectBlk;

    public Spy(float drawX, float drawY) {
        super("Dzil", ID, 0.0F, 0.0F, 90.0F, 120.0F, IMG, drawX, drawY);
        this.attackDmg = ATTACK_DMG;
        this.protectBlk = PROTECT_BLK;
        this.damage.add(new DamageInfo(this, this.attackDmg));
        this.block.add(new BlockInfo(this, this.protectBlk));
    }

    @Override
    public void usePreBattleAction() {
        addToTop(new ApplyPowerAction(this, this, new SpyPower(this, 1)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case DEFAULT:
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
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                addToBot(new ApplyPowerAction(this, this, new CompanionDexterityPower(this, PROTECT_PWR_AMT)));
                break;
            case SPECIAL:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    int distributedPoison = Math.floorDiv(SPECIAL_DEBUFF_AMT, AbstractDungeon.getCurrRoom().monsters.monsters.size());
                    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        addToBot(new ApplyPowerAction(mo, this, new PoisonPower(mo, this, distributedPoison)));
                    }
                    int remainderPoison = SPECIAL_DEBUFF_AMT % AbstractDungeon.getCurrRoom().monsters.monsters.size();
                    addToBot(new ApplyPowerAction(this, this, new PoisonPower(targetEnemy, this, remainderPoison)));
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
                int roll = MathUtils.random(1);
                if (roll == 0) {
                    addToBot(new SFXAction("VO_GREMLINSPAZZY_1A"));
                } else {
                    addToBot(new SFXAction("VO_GREMLINSPAZZY_1B"));
                }
                break;
            case ATTACK:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    if (hasPower(CompanionVigorPower.POWER_ID))
                        ((CompanionVigorPower) getPower(CompanionVigorPower.POWER_ID)).frenzyTrigger();
                    if (hasPower(SpyPower.POWER_ID)) {
                        for (int i = 0; i < getPower(SpyPower.POWER_ID).amount; i++) {
                            addToTop(new DamageAction(targetEnemy, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                        }
                    }
                }
                break;
            case PROTECT:
                addToTop(new ApplyPowerAction(this, this, new CompanionDexterityPower(this, PROTECT_PWR_AMT)));
                addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new NextTurnBlockPower(AbstractDungeon.player, this.block.get(0).output)));
                addToTop(new GainBlockAction(AbstractDungeon.player, this, this.block.get(0).output));
                if (hasPower(StaminaPower.POWER_ID))
                    getPower(StaminaPower.POWER_ID).onSpecificTrigger();
                break;
            case SPECIAL:
                if (targetEnemy != null && !targetEnemy.isDeadOrEscaped()) {
                    int remainderPoison = SPECIAL_DEBUFF_AMT % AbstractDungeon.getCurrRoom().monsters.monsters.size();
                    addToTop(new ApplyPowerAction(this, this, new PoisonPower(targetEnemy, this, remainderPoison)));
                    int distributedPoison = Math.floorDiv(SPECIAL_DEBUFF_AMT, AbstractDungeon.getCurrRoom().monsters.monsters.size());
                    for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        addToTop(new ApplyPowerAction(mo, this, new PoisonPower(mo, this, distributedPoison)));
                    }
                }
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
        if (hasPower(SpyPower.POWER_ID) && getPower(SpyPower.POWER_ID).amount > 1)
            setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, getPower(SpyPower.POWER_ID).amount, true, true);
        else
            setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, true);
    }

    @Override
    public void callProtect() {
        setMove(MOVES[2], PROTECT, Intent.DEFEND_BUFF, this.block.get(0).base, false);
    }

    @Override
    public void callSpecial() {
        getTarget();
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
                if (this.hasPower(SpyPower.POWER_ID) && this.getPower(SpyPower.POWER_ID).amount > 1) {
                    this.intentTip.body = INTENTS[1] + this.intentDmg + INTENTS[3] + this.getPower(SpyPower.POWER_ID).amount + INTENTS[4];
                } else if (this.hasPower(SpyPower.POWER_ID) && this.getPower(SpyPower.POWER_ID).amount == 1) {
                    this.intentTip.body = INTENTS[1] + this.intentDmg + INTENTS[2];
                } else {
                    //set up incorrectly
                    this.intentTip.body = "";
                }
                this.intentTip.img = getIntentImg();
                return;
            case PROTECT:
                this.intentTip.header = MOVES[2];
                this.intentTip.body = INTENTS[5] + this.intentBlk + INTENTS[6] + this.intentBlk + INTENTS[7] + PROTECT_PWR_AMT + INTENTS[8];
                this.intentTip.img = getIntentImg();
                return;
            case SPECIAL:
                this.intentTip.header = MOVES[3];
                this.intentTip.body = INTENTS[9] + SPECIAL_DEBUFF_AMT + INTENTS[10];
                this.intentTip.img = getIntentImg();
                return;
            case UNKNOWN:
                this.intentTip.header = MOVES[4];
                this.intentTip.body = INTENTS[11];
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
                    if (this.hasPower(SpyPower.POWER_ID) && this.getPower(SpyPower.POWER_ID).amount > 1) {
                        return INTENT_TOOLTIPS[0] + this.intentDmg + INTENT_TOOLTIPS[2] + this.getPower(SpyPower.POWER_ID).amount + INTENT_TOOLTIPS[3];
                    } else if (this.hasPower(SpyPower.POWER_ID) && this.getPower(SpyPower.POWER_ID).amount == 1) {
                        return INTENT_TOOLTIPS[0] + this.intentDmg + INTENT_TOOLTIPS[1];
                    } else {
                        //set up incorrectly
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
                    return INTENT_TOOLTIPS[8] + SPECIAL_DEBUFF_AMT + INTENT_TOOLTIPS[9];
                }
        }
        return "";
    }

    public void useTheCard(AbstractCard card, AbstractPlayer p, AbstractMonster m) {
        if (card instanceof Shiv) {
            addToBot(new ApplyPowerAction(this, this, new SpyPower(this, 1)));
            if (this.nextMove == ATTACK) {
                flashIntent();
                setMove(MOVES[1], ATTACK, Intent.ATTACK, this.damage.get(0).base, getPower(SpyPower.POWER_ID).amount, true, true);
                createIntent();
            }
        }
    }
}
