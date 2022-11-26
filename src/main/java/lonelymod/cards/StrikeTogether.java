package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.StrikeTogetherAction;

public class StrikeTogether extends AbstractEasyCard {
    public final static String ID = makeID("StrikeTogether");

    public StrikeTogether() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 8;
        tags.add(CardTags.STRIKE);
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new StrikeTogetherAction((AbstractCreature) m, this.damage, this.magicNumber));
    }

    @Override
    public void applyPowers() {
        this.baseDamage += 4 + this.timesUpgraded * 2;
        this.baseMagicNumber = this.baseDamage;
        super.applyPowers();
        this.magicNumber = this.damage;
        this.isMagicNumberModified = this.isDamageModified;
        this.baseDamage -= 4 + this.timesUpgraded * 2;
        super.applyPowers();
    }

    public void upp() {
        upgradeDamage(2);
        this.baseMagicNumber = this.baseDamage + 4 + this.timesUpgraded * 2;
        this.upgradedMagicNumber = this.upgradedDamage;
    }
}
