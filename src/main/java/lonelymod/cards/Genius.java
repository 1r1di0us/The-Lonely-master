package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.PlanAction;
import lonelymod.powers.StaminaPower;

public class Genius extends AbstractEasyCard {
    public final static String ID = makeID("Genius");

    public Genius() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 5;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PlanAction(this.magicNumber, this));
        addToBot(new ApplyPowerAction(p, p, new StaminaPower(p, this.secondMagic)));
    }

    public void upp() {
        upgradeMagicNumber(2);
        upgradeSecondMagic(2);
    }
}