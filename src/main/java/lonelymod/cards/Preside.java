package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.powers.PresidePower;

public class Preside extends AbstractEasyCard {
    public final static String ID = makeID("Preside");

    public Preside() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 10;
        baseSecondBlock = 5;
        baseMagicNumber = magicNumber = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        addToBot(new ApplyPowerAction(p, p, new PresidePower(p, this.magicNumber, this.secondBlock)));
    }

    public void upp() {
        upgradeBlock(2);
        upgradeSecondBlock(1);
    }
}
