package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;

import lonelymod.powers.DefyDeathPower;
import lonelymod.powers.GainStrengthBuffPower;

public class DefyDeath extends AbstractEasyCard {
    public final static String ID = makeID("DefyDeath");

    public DefyDeath() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 3;
        this.exhaust = true;
        this.tags.add(AbstractCard.CardTags.HEALING);
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new DefyDeathPower(p, 1), 1));
        addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, this.magicNumber), this.magicNumber));
        addToBot(new ApplyPowerAction(p, p, new GainStrengthBuffPower(p, this.secondMagic), this.secondMagic));
    }

    public void upp() {
        upgradeMagicNumber(1); upgradeSecondMagic(1);
    }
}