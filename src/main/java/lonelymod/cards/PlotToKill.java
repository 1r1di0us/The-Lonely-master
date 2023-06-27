package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EnergizedPower;

import lonelymod.actions.PlanAction;

public class PlotToKill extends AbstractEasyCard {
    public final static String ID = makeID("PlotToKill");

    public PlotToKill() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);
        baseDamage = 3;
        baseMagicNumber = magicNumber = 5;
        baseSecondMagic = secondMagic = 1;
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        AbstractDungeon.actionManager.addToBottom(new PlanAction(this.magicNumber, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new EnergizedPower(p, secondMagic), secondMagic));
    }

    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(2);
    }
}