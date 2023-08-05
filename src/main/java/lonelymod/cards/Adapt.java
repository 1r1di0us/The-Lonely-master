package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.AdaptFollowUpAction;

public class Adapt extends AbstractEasyCard {
    public final static String ID = makeID("Adapt");

    public Adapt() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        baseBlock = 5;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        addToBot(new DrawCardAction(this.magicNumber, new AdaptFollowUpAction(this.block)));
    }

    public void upp() {
        upgradeBlock(2);
    }
}
