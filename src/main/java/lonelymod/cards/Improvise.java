package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.PlanAction;

public class Improvise extends AbstractEasyCard {
    public final static String ID = makeID("Improvise");

    public Improvise() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        baseBlock = 3;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new PlanAction(this.magicNumber, this, this.block));
    }

    public void upp() {
        upgradeMagicNumber(1);
    }
}
