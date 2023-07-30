package lonelymod.cards;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.ExponentialForceAction;

import static lonelymod.LonelyMod.makeID;

public class ExponentialForce extends AbstractEasyCard {
    public final static String ID = makeID("ExponentialForce");
    public ExponentialForce() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        this.baseDamage = 10;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ExponentialForceAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), this.uuid));
    }

    @Override
    public void upp() {
        upgradeDamage(3);
    }
}
