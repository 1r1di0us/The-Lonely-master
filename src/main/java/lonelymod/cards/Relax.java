package lonelymod.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RemoveAllBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static lonelymod.LonelyMod.makeID;

public class Relax extends AbstractEasyCard {
    public final static String ID = makeID("Relax");

    public Relax() {
        super(ID, 1, AbstractCard.CardType.SKILL, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 5;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(this.magicNumber));
        int number = Math.floorDiv(p.currentBlock, this.secondMagic);
        addToBot(new GainEnergyAction(number));
        addToBot(new RemoveAllBlockAction(p, p));
    }

    public void upp() {
        upgradeMagicNumber(1);
    }
}
