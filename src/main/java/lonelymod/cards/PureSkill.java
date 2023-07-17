package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import lonelymod.actions.PlanAction;
import lonelymod.powers.StaminaPower;

public class PureSkill extends AbstractEasyCard {
    public final static String ID = makeID("PureSkill");

    int basePlanAmount;

    public PureSkill() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 2;
        baseThirdMagic = thirdMagic = 1;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new VigorPower(p, this.magicNumber)));
        addToBot(new ApplyPowerAction(p, p, new StaminaPower(p, this.secondMagic)));
        addToBot(new PlanAction(this.thirdMagic));
    }

    public void upp() {
        upgradeMagicNumber(1);
        upgradeSecondMagic(1);
        upgradeThirdMagic(1);
    }
}