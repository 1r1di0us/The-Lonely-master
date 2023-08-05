package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.ImproviseAction;

public class Improvise extends AbstractEasyCard {
    public static final String ID = makeID("Improvise");
    
    public Improvise() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.exhaust = true;
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ImproviseAction(this.upgraded));
    }
    
    public void upp() {
        uDesc();
    }
}
