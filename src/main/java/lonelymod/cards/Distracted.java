package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import lonelymod.powers.AddCardToHandPower;
import lonelymod.powers.DeenergizedPower;

public class Distracted extends AbstractEasyCard {
    public final static String ID = makeID("Distracted");

    public Distracted() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 4;
        this.baseSecondMagic = this.secondMagic = 1;
        this.cardsToPreview = new Overreaction();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new VigorPower(p, this.magicNumber)));
        addToBot(new ApplyPowerAction(p, p, new DeenergizedPower(p, this.secondMagic)));
        addToBot(new ApplyPowerAction(p, p, new AddCardToHandPower(p, 1, new Overreaction(), false)));
    }

    public void upp() {
        upgradeMagicNumber(3);
    }
}
