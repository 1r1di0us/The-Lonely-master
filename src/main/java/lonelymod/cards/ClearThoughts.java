package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.SetAutoplayAction;


public class ClearThoughts extends AbstractEasyCard {
    public final static String ID = makeID("ClearThoughts");

    public ClearThoughts() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(p, this.magicNumber));
        for (int i=0; i < this.magicNumber; i++) {
            if (AutoplayField.autoplay.get(p.drawPile.getNCardFromTop(i))) {
                AutoplayField.autoplay.set(p.drawPile.getNCardFromTop(i), false);
                addToBot(new SetAutoplayAction(p.drawPile.getNCardFromTop(i)));
            }
        }
    }

    public void upp() {
        upgradeMagicNumber(1);
    }
}
