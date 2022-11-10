package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PanicAttack extends AbstractEasyCard {
    public final static String ID = makeID("PanicAttack");

    public PanicAttack() {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.SELF);
        baseDamage = 5;
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 1;
        exhaust = true;
        isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageRandomEnemyAction(new DamageInfo(p, damage), AttackEffect.BLUNT_LIGHT));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, magicNumber));
        if (upgraded) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(secondMagic));
        }
    }

    public void upp() {
        upgradeDamage(2);
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
