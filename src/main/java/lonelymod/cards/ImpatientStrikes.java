package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
//import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ImpatientStrikes extends AbstractEasyCard {
    public final static String ID = makeID("ImpatientStrikes");

    public ImpatientStrikes() {
        super(ID, 3, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 9;
        baseMagicNumber = magicNumber = 2;
        tags.add(CardTags.STRIKE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }
    }

    @Override
    public void triggerOnAbility() {
        setCostForTurn(this.costForTurn - 1);
    }

    //@Override
    //public void triggerWhenDrawn() {
    //    super.triggerWhenDrawn();
    //    setCostForTurn(this.cost - GameActionManager.abilitiesCalledThisTurn); //Its gonna stay broken until I make actual companions
    //}

    public void upp() {
        upgradeDamage(3);
    }
}