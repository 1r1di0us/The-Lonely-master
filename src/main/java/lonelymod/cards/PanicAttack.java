package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PanicAttack extends AbstractEasyCard {
    public final static String ID = makeID("PanicAttack");

    public PanicAttack() {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);
        baseDamage = 7;
        baseMagicNumber = magicNumber = 1;
        baseSecondMagic = secondMagic = 1;
        exhaust = true;
        isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.BLUNT_LIGHT);
        addToBot(new DrawCardAction(p, magicNumber));
        addToBot(new GainEnergyAction(secondMagic));
    }

    public void upp() {
        upgradeDamage(2);
        upgradeSecondMagic(1);
        uDesc();
    }
}
