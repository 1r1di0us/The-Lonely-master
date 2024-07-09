package lonelymod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.powers.HelpPower;

import static lonelymod.LonelyMod.makeID;

public class Help extends AbstractEasyCard {
    public final static String ID = makeID("Help");

    public Help() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        this.tags.add(Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new HelpPower(p, this.magicNumber)));
    }

    public void upp() {
        upgradeMagicNumber(1);
    }
}
