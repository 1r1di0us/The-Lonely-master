package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;

import lonelymod.actions.PlanAction;

public class DeviousPloy extends AbstractEasyCard {
    public final static String ID = makeID("DeviousPloy");

    public DeviousPloy() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);
        baseDamage = 7;
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 1;
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        addToBot(new PlanAction(this.magicNumber, this));
        addToBot(new ApplyPowerAction(p, p, new EnergizedPower(p, this.secondMagic)));
        //addToBot(new ApplyPowerAction(p, p, new EnergizedYellowPower(p, this.secondMagic)));
        // I don't have a good asset for this.
    }

    public void upp() {
        upgradeDamage(3);
        upgradeMagicNumber(1);
    }
}