package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.ImproviseAction;

public class Improvise extends AbstractEasyCard {
    public static final String ID = makeID("Improvise");
    
    public Improvise() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 5;
        this.exhaust = true;
        //unfortunately it does partially block the eye button
        //might rename this to Adapt and Rename adapt improvise solely based on duct tape meme
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ImproviseAction(this.upgraded, this.magicNumber));
    }
    
    public void upp() {
        uDesc();
    }
}
