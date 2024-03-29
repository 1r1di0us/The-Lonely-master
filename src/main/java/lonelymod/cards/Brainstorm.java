package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.BrainstormAction;

public class Brainstorm extends AbstractEasyCard {
    public final static String ID = makeID("Brainstorm");

    public Brainstorm() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = 13;
        this.isMultiDamage = true;
        baseMagicNumber = magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        allDmg(AttackEffect.LIGHTNING);
        addToBot(new BrainstormAction(this.magicNumber));
    }

    public void upp() {
        upgradeDamage(4);
    }
}
