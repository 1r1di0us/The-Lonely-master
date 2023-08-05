package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import lonelymod.fields.ReturnField;

public class TwistedChant extends AbstractEasyCard {
    public final static String ID = makeID("TwistedChant");

    public TwistedChant() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 5;
        baseSecondMagic = secondMagic = 1;
        this.isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, this.magicNumber), this.magicNumber));
        addToBot(new MakeTempCardInDiscardAction(new Dazed(), this.secondMagic));
        if (upgraded) {
            ReturnField.willReturn.set(this, true);
        }
    }

    public void upp() {
        uDesc();
    }
}
