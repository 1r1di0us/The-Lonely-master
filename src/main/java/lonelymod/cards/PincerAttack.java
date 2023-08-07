package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.PincerAttackAction;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;

public class PincerAttack extends AbstractEasyCard {
    public final static String ID = makeID("PincerAttack");

    public PincerAttack() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 8;
        //baseMagicNumber = magicNumber = 8;
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PincerAttackAction(m, this.damage));
    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null && CompanionField.currCompanion.get(AbstractDungeon.player).move.nextMove == AbstractCompanion.ATTACK)
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
    }

    /*@Override
    public void applyPowers() {
        this.baseMagicNumber = this.baseDamage;
        super.applyPowers();
        this.magicNumber = this.damage;
        this.isMagicNumberModified = this.isDamageModified;
        super.applyPowers();
    }
*/
    public void upp() {
        upgradeDamage(2);
        //this.baseMagicNumber = this.baseDamage;
        //this.upgradedMagicNumber = this.upgradedDamage;
    }
}
