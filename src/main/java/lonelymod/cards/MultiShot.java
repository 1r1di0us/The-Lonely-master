package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.MultiShotPower;

public class MultiShot extends AbstractEasyCard {
    public final static String ID = makeID("MultiShot");

    public MultiShot() {
        super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new MultiShotPower(p, magicNumber, secondMagic), magicNumber));
    }
    
    public void upp() {
        upgradeMagicNumber(1);
    }
}
