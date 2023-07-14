package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.CallAttackAction;
import lonelymod.powers.AttackNextTurnPower;


public class Kill extends AbstractEasyCard {
    public final static String ID = makeID("Kill");

    public Kill() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 5;
        magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        addToBot(new CallAttackAction());
        if (upgraded)
            addToBot(new ApplyPowerAction(p, p, new AttackNextTurnPower(p, this.magicNumber)));
    }

    public void upp() {
        upgradeMagicNumber(1);
        uDesc();
    }
}