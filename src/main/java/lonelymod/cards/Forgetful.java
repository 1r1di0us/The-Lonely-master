package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.powers.AddCardToHandPower;
import lonelymod.powers.NewDrawReductionPower;

public class Forgetful extends AbstractEasyCard {
    public final static String ID = makeID("Forgetful");

    public Forgetful() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 1;
        this.cardsToPreview = new PanicAttack();
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new NewDrawReductionPower(p, this.magicNumber, false)));
        addToBot(new ApplyPowerAction(p, p, new AddCardToHandPower(p, 1, new PanicAttack(), false)));
    }

    public void upp() {
        this.exhaust = false;
        uDesc();
    }
}
