package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.ImproviseFollowUpAction;
import lonelymod.actions.PlanAction;

public class Improvise extends AbstractEasyCard {
    public final static String ID = makeID("Improvise");

    public Improvise() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        baseBlock = 3;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(this.magicNumber, new ImproviseFollowUpAction(this.block)));
    }

    public void upp() {
        upgradeBlock(2);
    }
}
