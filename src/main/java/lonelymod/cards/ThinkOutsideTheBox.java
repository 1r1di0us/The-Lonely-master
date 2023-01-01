package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.ThinkOutsideTheBoxAction;

public class ThinkOutsideTheBox extends AbstractEasyCard {
    public static final String ID = makeID("ThinkOutsideTheBox");
    
    public ThinkOutsideTheBox() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true;
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new ThinkOutsideTheBoxAction(this.upgraded));
    }
    
    public void upp() {
        uDesc();
    }
}
