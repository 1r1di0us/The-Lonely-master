package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class Dominate extends AbstractEasyCard {
    public final static String ID = makeID("Dominate");

    public Dominate() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 0;
        baseMagicNumber = magicNumber = 0;
        baseSecondMagic = secondMagic = 4;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.BLUNT_LIGHT);
        if (this.damage - m.currentBlock <= this.magicNumber) {
            addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -secondMagic)));
            //if (m != null && !m.hasPower("Artifact"))
            //    addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, secondMagic)));
        }
    }

    public void upp() {
        upgradeMagicNumber(5);
        uDesc();
    }
}
