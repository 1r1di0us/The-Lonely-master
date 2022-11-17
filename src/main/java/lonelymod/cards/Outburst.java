package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.OutburstAction;

public class Outburst extends AbstractEasyCard {
    public final static String ID = makeID("Outburst");

    public Outburst() {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        this.exhaust = true;
        this.isEthereal = true;
        baseMagicNumber = magicNumber = 4;
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new OutburstAction(magicNumber, this.upgraded));
    }

    public void upp() {
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
