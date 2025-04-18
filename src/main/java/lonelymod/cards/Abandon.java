package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.AbandonAction;

public class Abandon extends AbstractEasyCard {
    public final static String ID = makeID("Abandon");

    public Abandon() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
        this.isEthereal = true;

        this.tags.add(AbstractCard.CardTags.HEALING);
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AbandonAction());
    }

    public void upp() {
        upgradeBaseCost(0);
    }
}
