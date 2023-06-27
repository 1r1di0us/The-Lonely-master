package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;

public class OvercomeWeakness extends AbstractEasyCard {
    public final static String ID = makeID("OvercomeWeakness");

    public OvercomeWeakness() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 1;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        int amount = 0;
        for (AbstractPower power : p.powers) {
            if (power.type == PowerType.DEBUFF) {
                amount += power.amount;
            }
        }
        if (amount > 0) {
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, amount), amount));
            addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, amount), amount));
        }
    }

    public void upp() {
        upgradeBaseCost(1);
    }
}
