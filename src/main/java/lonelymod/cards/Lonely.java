package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import java.util.ArrayList;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Lonely extends AbstractEasyCard {
    public static final String ID = makeID("Lonely");
    
    public Lonely() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        AutoplayField.autoplay.set(this, true);
        //no MultiCardPreview because that looks ugly as heck
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> cardChoices = new ArrayList<>();
        cardChoices.add(new Bravery());
        cardChoices.add(new Desperation());
        cardChoices.add(new Resolve());
        if (this.upgraded)
        for (AbstractCard c : cardChoices)
            c.upgrade();
        addToBot((AbstractGameAction)new ChooseOneAction(cardChoices));
        //after "choosing" a card, that card's onChoseThisOption() activates.
    }
    
    public void upp() {
        upgradeBaseCost(1);
    }
}
