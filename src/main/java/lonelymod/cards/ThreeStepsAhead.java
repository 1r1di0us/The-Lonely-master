package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.ThreeStepsAheadPower;

public class ThreeStepsAhead extends AbstractEasyCard {
    public final static String ID = makeID("ThreeStepsAhead");

    public ThreeStepsAhead() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ThreeStepsAheadPower(p, magicNumber), magicNumber));
    }
    
    public void upp() {
        upgradeBaseCost(0);
    }
}
