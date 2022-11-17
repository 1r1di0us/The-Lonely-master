package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import lonelymod.powers.ShockAndAwePower;

public class FeignWeakness extends AbstractEasyCard {
    public final static String ID = makeID("FeignWeakness");

    public FeignWeakness() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.SELF_AND_ENEMY);
        baseDamage = 1;
        baseMagicNumber = magicNumber = 0;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        DamageInfo dInfo = new DamageInfo(p, this.damage);
        addToBot(new DamageAction(m, dInfo, AttackEffect.BLUNT_LIGHT));
        if (dInfo.output <= 1) {
            addToBot(new ApplyPowerAction(p, p, new ShockAndAwePower(p, m, 1)));
            if (this.upgraded) {
                addToBot(new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false)));
            }
        }
    }

    public void upp() {
        upgradeMagicNumber(5);
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
