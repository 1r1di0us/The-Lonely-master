package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.fields.ReturnField;
import lonelymod.powers.OverflowingQuiverPower;

public class OverflowingQuiver extends AbstractEasyCard {
    public final static String ID = makeID("OverflowingQuiver");

    public OverflowingQuiver() {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);
        baseDamage = 3;
        this.isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new OverflowingQuiverPower(p, 1)));
        ReturnField.willReturn.set(this, true);
    }

    public void upp() {
        upgradeDamage(1);
    }
}
