package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PanicAttack extends AbstractEasyCard {
    public final static String ID = makeID("PanicAttack");

    public PanicAttack() {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.SELF);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 2;
        exhaust = true;
        isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AttackDamageRandomEnemyAction(this, AttackEffect.BLUNT_LIGHT));
        addToBot(new GainEnergyAction(magicNumber));
    }

    public void upp() {
        upgradeDamage(3);
    }
}
