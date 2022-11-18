package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.PlanAction;

public class ALittlePloy extends AbstractEasyCard {
    public final static String ID = makeID("ALittlePloy");

    public ALittlePloy() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 1;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new PlanAction(this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.secondMagic));
    }

    public void upp() {
        upgradeMagicNumber(1);
        upgradeSecondMagic(1);
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}