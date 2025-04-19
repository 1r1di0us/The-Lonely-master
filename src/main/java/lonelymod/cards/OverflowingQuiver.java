package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.fields.ReturnField;

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
        baseDamage = 4;
        this.isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        addToBot(new MakeTempCardInDrawPileAction(this.makeStatEquivalentCopy(), 1, true, true, false));
        if (upgraded) {
            ReturnField.willReturn.set(this, true);
        }
    }

    public void upp() {
        uDesc();
    }
}
