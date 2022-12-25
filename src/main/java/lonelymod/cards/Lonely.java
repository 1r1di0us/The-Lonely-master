package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.AutoplayWaitAction;
import lonelymod.actions.LonelyAction;

public class Lonely extends AbstractEasyCard {
    public static final String ID = makeID("Lonely");
    
    public Lonely() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        AutoplayField.autoplay.set(this, true);
        //no MultiCardPreview because that looks ugly as heck
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AutoplayWaitAction(1.0f));
        addToBot(new LonelyAction(upgraded));
    }
    
    public void upp() {
        upgradeBaseCost(1);
    }
}
