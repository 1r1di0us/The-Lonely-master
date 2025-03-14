package lonelymod.powers;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import static lonelymod.LonelyMod.makeID;


public class EnemyConstrictedPower extends AbstractPower implements CloneablePowerInterface {
    //literally the exact same as normal constricted, I just am editing the text.

    public static final String POWER_ID = makeID("EnemyConstrictedPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    public AbstractCreature source;

    public EnemyConstrictedPower(AbstractCreature target, AbstractCreature source, int fadeAmt) {
        this.name = NAME;// 18
        this.ID = "Constricted";// 19
        this.owner = target;// 20
        this.source = source;// 21
        this.amount = fadeAmt;// 22
        this.updateDescription();// 23
        this.loadRegion("constricted");// 24
        this.type = PowerType.DEBUFF;// 25
        this.priority = 105;// 28
    }// 29

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_CONSTRICTED", 0.05F);// 33
    }// 34

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];// 38
    }// 39

    public void atEndOfTurn(boolean isPlayer) {
        this.flashWithoutSound();// 43
        this.playApplyPowerSfx();// 44
        this.addToBot(new DamageAction(this.owner, new DamageInfo(this.source, this.amount, DamageType.THORNS)));// 45
    }// 46

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings("Constricted");// 11
        NAME = powerStrings.NAME;// 12
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;// 13
    }

    public AbstractPower makeCopy() {
        return new EnemyConstrictedPower(this.owner, this.source, this.amount);
    }
}

