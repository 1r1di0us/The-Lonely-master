package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;

public class Overcome extends AbstractEasyCard {
    public final static String ID = makeID("Overcome");

    public Overcome() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 10;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractPower power : p.powers) {
            if (power.type == PowerType.DEBUFF) {
                blck();
            }
        }
    }

    public void upp() {
        upgradeBlock(4);
    }
}
