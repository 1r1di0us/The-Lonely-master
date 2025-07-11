package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.FetchPower;
import lonelymod.powers.TargetPower;

public class Fetch extends AbstractEasyCard {
    public final static String ID = makeID("Fetch");

    public Fetch() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.baseDamage = 8;
        this.baseMagicNumber = this.magicNumber = 1;
        this.baseSecondMagic = this.secondMagic = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.BLUNT_LIGHT);
        addToBot(new ApplyPowerAction(m, p, new TargetPower(m, this.magicNumber, false)));
        addToBot(new ApplyPowerAction(p, p, new FetchPower(p, this.secondMagic)));
    }

    public void upp() {
        upgradeDamage(2);
        upgradeSecondMagic(1);
    }
}
