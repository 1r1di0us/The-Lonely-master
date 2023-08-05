package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.interfaces.TriggerOnCallMoveInterface;

public class VentFrustration extends AbstractEasyCard implements TriggerOnCallMoveInterface {
    public final static String ID = makeID("VentFrustration");

    public static int movesCalledThisTurn = 0;

    public VentFrustration() {
        super(ID, 3, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 8;
        baseMagicNumber = magicNumber = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        if (movesCalledThisTurn >= 3)
            setCostForTurn(0);
        else
            setCostForTurn(this.cost - movesCalledThisTurn);
    }

    @Override
    public void triggerOnCallMove(byte move) {
        if (movesCalledThisTurn >= 3)
            setCostForTurn(0);
        else
            setCostForTurn(this.cost - movesCalledThisTurn);
    }

    public void upp() {
        upgradeDamage(2);
    }
}