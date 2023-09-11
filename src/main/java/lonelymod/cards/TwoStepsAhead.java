package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.TwoStepsAheadPower;

public class TwoStepsAhead extends AbstractEasyCard {
    public final static String ID = makeID("TwoStepsAhead");

    public TwoStepsAhead() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new TwoStepsAheadPower(p, magicNumber), magicNumber));
    }
    
    public void upp() {
        upgradeMagicNumber(1);
    }
}
