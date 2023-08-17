package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.AbandonAction;

public class Abandon extends AbstractEasyCard {
    public final static String ID = makeID("Abandon");

    public Abandon() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = magicNumber = 1;
        this.exhaust = true;
        this.isEthereal = true;

        this.tags.add(AbstractCard.CardTags.HEALING);
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (magicNumber > 0)
            addToBot(new DrawCardAction(p, magicNumber));
        addToBot(new AbandonAction());
    }

    public void upp() {
        upgradeMagicNumber(-1);
        uDesc();
    }
}
