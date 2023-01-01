package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.fields.ReturnField;
import lonelymod.interfaces.TriggerOnReturnInterface;

public class OverflowingQuiver extends AbstractEasyCard implements TriggerOnReturnInterface {
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
        ReturnField.willReturn.set(this, true);
    }

    @Override
    public void triggerOnReturn() {
        addToBot(new MakeTempCardInHandAction(this.makeStatEquivalentCopy()));
    }

    public void upp() {
        upgradeDamage(1);
    }
}
