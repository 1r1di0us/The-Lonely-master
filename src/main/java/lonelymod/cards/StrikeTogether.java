package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.orbs.WolfAttackAction;

public class StrikeTogether extends AbstractEasyCard {
    public final static String ID = makeID("StrikeTogether");

    public StrikeTogether() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 12;
        tags.add(CardTags.STRIKE);
    }

    @Override
    public boolean canUse(AbstractPlayer p , AbstractMonster m) {
        if (p.orbs.get(0) instanceof WolfAttackAction) {
            return true;
        }
        return false;
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    public void upp() {
        upgradeDamage(4);
    }
}
