package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.GashAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.fields.ReturnField;
import lonelymod.interfaces.TriggerOnPlanInterface;

public class EndlessOnslaught extends AbstractEasyCard implements TriggerOnPlanInterface {
    public final static String ID = makeID("EndlessOnslaught");

    public EndlessOnslaught() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 9;
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void triggerOnPlan(boolean thisCardPlanned) {
        if (!thisCardPlanned) {
            addToBot(new GashAction(this, magicNumber));
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        ReturnField.willReturn.set(this, true);
    }

    public void upp() {
        upgradeDamage(3);
        upgradeMagicNumber(1);
    }
}
