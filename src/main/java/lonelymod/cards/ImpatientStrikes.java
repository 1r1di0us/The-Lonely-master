package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.interfaces.TriggerOnAbilityInterface;

public class ImpatientStrikes extends AbstractEasyCard implements TriggerOnAbilityInterface {
    public final static String ID = makeID("ImpatientStrikes");
    public static int attackCounter;

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
    public void triggerOnAbility(int abilityType) {
        if (abilityType == 2) {
            attackCounter++;
            setCostForTurn(this.cost - attackCounter);
        }
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();
        setCostForTurn(this.cost - attackCounter);
    }

    public void upp() {
        upgradeDamage(3);
    }
}