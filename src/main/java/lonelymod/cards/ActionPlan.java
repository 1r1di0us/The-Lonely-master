package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import com.megacrit.cardcrawl.powers.EnergizedPower;
import lonelymod.actions.CallProtectAction;
import lonelymod.powers.AttackNextTurnPower;

public class ActionPlan extends AbstractEasyCard {
    public final static String ID = makeID("ActionPlan");

    public ActionPlan() {
        super(ID, 2, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        //magic Numbers will start out at -1 sometimes if you just say baseMagicNumber = #
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new CallProtectAction());
        addToBot(new ApplyPowerAction(p, p, new EnergizedPower(p, this.magicNumber))); //might need my own energized power in the future
        addToBot(new ApplyPowerAction(p, p, new AttackNextTurnPower(p, 1)));
    }

    public void upp() {
        upgradeMagicNumber(1);
        uDesc();
    }
}
