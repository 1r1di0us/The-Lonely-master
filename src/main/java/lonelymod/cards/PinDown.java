package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.PinDownPlusPower;
import lonelymod.powers.PinDownPower;

public class PinDown extends AbstractEasyCard{
    public final static String ID = makeID("PinDown");

    public PinDown() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded) addToBot(new ApplyPowerAction(p, p, new PinDownPower(p, this.magicNumber), this.magicNumber));
        else addToBot(new ApplyPowerAction(p, p, new PinDownPlusPower(p, this.magicNumber), this.magicNumber));
    }

    public void upp() {
        uDesc();
    }
}