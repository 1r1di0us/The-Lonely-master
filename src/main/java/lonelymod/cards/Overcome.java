package lonelymod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.powers.OvercomePower;

import static lonelymod.LonelyMod.makeID;

public class Overcome extends AbstractEasyCard {
    public final static String ID = makeID("Overcome");

    public Overcome() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 8;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new OvercomePower(p, this.magicNumber)));
    }

    public void upp() {
        this.isInnate = true;
        uDesc();
    }
}
