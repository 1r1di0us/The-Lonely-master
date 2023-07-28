package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;

import lonelymod.actions.PlanAction;

public class DeviousPloy extends AbstractEasyCard {
    public final static String ID = makeID("DeviousPloy");

    public DeviousPloy() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 2;
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        addToBot(new PlanAction(this.magicNumber, this));
        addToBot(new DrawCardAction(this.secondMagic));
    }

    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
    }
}