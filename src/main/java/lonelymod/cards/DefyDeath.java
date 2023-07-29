package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;

import com.megacrit.cardcrawl.powers.GainStrengthPower;
import lonelymod.powers.DefyDeathPower;

public class DefyDeath extends AbstractEasyCard {
    public final static String ID = makeID("DefyDeath");

    public DefyDeath() {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 5;
        baseSecondMagic = secondMagic = 6;
        this.exhaust = true;
        this.tags.add(AbstractCard.CardTags.HEALING);
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new DefyDeathPower(p, 1), 1));
        addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, this.magicNumber), this.magicNumber));
        addToBot(new ApplyPowerAction(p, p, new GainStrengthPower(p, this.secondMagic), this.secondMagic));
    }

    public void upp() {
        upgradeBaseCost(2);
    }
}