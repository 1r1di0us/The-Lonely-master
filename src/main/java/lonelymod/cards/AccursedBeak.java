package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import lonelymod.actions.AccursedBeakAction;

public class AccursedBeak extends AbstractEasyCard {
    public final static String ID = makeID("AccursedBeak");

    public AccursedBeak() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 3;
        baseMagicNumber = magicNumber = 1;
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AttackEffect.BLUNT_LIGHT);
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, -magicNumber)));
        addToBot(new ApplyPowerAction(p, p, new GainStrengthPower(p, magicNumber)));
        addToBot(new AccursedBeakAction(p, m, this));
    }

    public void upp() {
        upgradeDamage(2);
    }
}
