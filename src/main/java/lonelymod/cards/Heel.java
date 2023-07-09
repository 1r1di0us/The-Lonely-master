package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.CallProtectAction;

public class Heel extends AbstractEasyCard {
    public final static String ID = makeID("Heel");

    public Heel() {
        super(ID, 2, CardType.ATTACK, CardRarity.BASIC, CardTarget.SELF_AND_ENEMY);
        this.baseDamage = 6;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.BLUNT_LIGHT);
        addToBot(new CallProtectAction());
    }

    public void upp() {
        upgradeDamage(3);
    }
}