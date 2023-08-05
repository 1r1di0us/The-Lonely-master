package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawReductionPower;
import lonelymod.powers.AddCardToHandPower;

public class Forgetful extends AbstractEasyCard {
    public final static String ID = makeID("Forgetful");

    public Forgetful() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 1;
        this.cardsToPreview = new PanicAttack();
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new DrawReductionPower(p, this.magicNumber)));
        addToBot(new ApplyPowerAction(p, p, new AddCardToHandPower(p, 1, new PanicAttack(), false)));
    }

    public void upp() {
        this.exhaust = false;
        uDesc();
    }
}
