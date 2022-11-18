package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;

import lonelymod.powers.DefyDeathPower;

public class DefyDeath extends AbstractEasyCard {
    public final static String ID = makeID("DefyDeath");

    public DefyDeath() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 0;
        this.exhaust = true;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DefyDeathPower(p, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, magicNumber), magicNumber));
        if (upgraded)
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new EnergizedPower(p, secondMagic), secondMagic));
    }

    public void upp() {
        upgradeSecondMagic(1);
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}