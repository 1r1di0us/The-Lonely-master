package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import com.megacrit.cardcrawl.powers.RitualPower;
import lonelymod.actions.DarkRitualAction;

public class DarkRitual extends AbstractEasyCard {
    public final static String ID = makeID("DarkRitual");

    public DarkRitual() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 0;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        //Rest in peace Hemokinesis effect on self
        addToBot(new DarkRitualAction(2, this.magicNumber));
    }

    public void upp() {
        upgradeMagicNumber(1);
        uDesc();
    }
}
