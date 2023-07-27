package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;

public class WickedTalons extends AbstractEasyCard {
    public final static String ID = makeID("WickedTalons");

    public WickedTalons() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 4;
        baseSecondMagic = secondMagic = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        addToBot(new ApplyPowerAction(p, p, new GainStrengthPower(p, magicNumber)));
        addToBot(new ApplyPowerAction(p, p, new LoseDexterityPower(p, secondMagic)));
    }

    public void upp() {
        upgradeDamage(2);
    }
}