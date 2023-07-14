package lonelymod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.CallAttackAction;
import lonelymod.fields.ReturnField;
import lonelymod.powers.TargetPower;

import static lonelymod.LonelyMod.makeID;

public class OnTheHunt extends AbstractEasyCard {
    public final static String ID = makeID("OnTheHunt");

    public OnTheHunt() {
        super(ID, 1, AbstractCard.CardType.ATTACK, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ENEMY);
        baseDamage = 6;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        if (m.hasPower(TargetPower.POWER_ID)) {
            addToBot(new CallAttackAction());
        }
        ReturnField.willReturn.set(this, true);
    }

    public void upp() {
        upgradeDamage(3);
    }
}
