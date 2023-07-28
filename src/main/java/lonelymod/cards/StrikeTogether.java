package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.StrikeTogetherAction;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;

public class StrikeTogether extends AbstractEasyCard {
    public final static String ID = makeID("StrikeTogether");

    public StrikeTogether() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 5;
        baseMagicNumber = magicNumber = 10;
        tags.add(CardTags.STRIKE);
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new StrikeTogetherAction(m, this.damage, this.magicNumber));
    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null && CompanionField.currCompanion.get(AbstractDungeon.player).move.nextMove == AbstractCompanion.ATTACK)
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public void applyPowers() {
        this.baseDamage += 5 + this.timesUpgraded * 3;
        this.baseMagicNumber = this.baseDamage;
        super.applyPowers();
        this.magicNumber = this.damage;
        this.isMagicNumberModified = this.isDamageModified;
        this.baseDamage -= 5 + this.timesUpgraded * 3;
        super.applyPowers();
    }

    public void upp() {
        upgradeDamage(2);
        this.baseMagicNumber = this.baseDamage + 5 + this.timesUpgraded * 3;
        this.upgradedMagicNumber = this.upgradedDamage;
    }
}
