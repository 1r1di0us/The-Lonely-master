package lonelymod.cards.deprecated;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import lonelymod.cards.AbstractEasyCard;
import lonelymod.fields.ReturnField;

public class DEPRECATEDApprehend extends AbstractEasyCard {
    public final static String ID = makeID("Apprehend");

    public DEPRECATEDApprehend() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 6;
        baseMagicNumber = magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        addToBot(new ApplyPowerAction(m, p, new StrengthPower(m, -magicNumber)));
        if (m != null && !m.hasPower("Artifact"))
            addToBot(new ApplyPowerAction(m, p, new GainStrengthPower(m, magicNumber)));
        ReturnField.willReturn.set(this, true);
    }

    public void upp() {
        upgradeDamage(1);
        upgradeMagicNumber(1);
    }
}