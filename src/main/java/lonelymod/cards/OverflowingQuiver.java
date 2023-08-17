package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.fields.ReturnField;
import lonelymod.interfaces.TriggerOnReturnInterface;
import lonelymod.powers.AddCardToHandPower;

public class OverflowingQuiver extends AbstractEasyCard {
    public final static String ID = makeID("OverflowingQuiver");

    public OverflowingQuiver(boolean upgraded) {
        this();
        if (upgraded) {
            this.upgrade();
        }
    }

    public OverflowingQuiver() {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);
        baseDamage = 3;
        this.isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        addToBot(new ApplyPowerAction(p, p, new AddCardToHandPower(p, 1, this.makeStatEquivalentCopy(), false)));
        ReturnField.willReturn.set(this, true);
    }

    public void upp() {
        upgradeDamage(1);
    }
}
