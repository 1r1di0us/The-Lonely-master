package lonelymod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.powers.MiseryPower;

import static lonelymod.LonelyMod.makeID;

public class Misery extends AbstractEasyCard {
    public final static String ID = makeID("Misery");

    public Misery() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseBlock = 10;
        baseMagicNumber = magicNumber = 4;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new MiseryPower(p, this.magicNumber, this.upgraded)));
        blck();
    }

    public void upp() {
        upgradeMagicNumber(2);
        //upgradeBlock(2);
        uDesc();
    }
}
