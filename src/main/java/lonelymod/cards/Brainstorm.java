package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.PlanAction;

public class Brainstorm extends AbstractEasyCard {
    public final static String ID = makeID("Brainstorm");

    public Brainstorm() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseDamage = 12;
        this.isMultiDamage = true;
        baseMagicNumber = magicNumber = 6;
        baseSecondMagic = secondMagic = 2;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        allDmg(AttackEffect.LIGHTNING);
        addToBot(new PlanAction(this.magicNumber));
        addToBot(new DrawCardAction(this.secondMagic));
    }

    public void upp() {
        upgradeDamage(3);
        upgradeMagicNumber(2);
    }
}