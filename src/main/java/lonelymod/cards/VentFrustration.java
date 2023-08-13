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
        baseDamage = 7;
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
        setCostForTurn(this.cost - movesCalledThisTurn);
    }

    public void atTurnStart() {
        movesCalledThisTurn = 0;
        resetAttributes();
        applyPowers();
    }

    @Override
    public void triggerOnCallMove(byte move, byte prevMove) {
        setCostForTurn(this.costForTurn - 1);
    }

    public void upp() {
        upgradeDamage(2);
    }
}