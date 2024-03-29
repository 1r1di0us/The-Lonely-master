package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.MuscleMemoryPower;

public class MuscleMemory extends AbstractEasyCard {
    public final static String ID = makeID("MuscleMemory");

    public MuscleMemory() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new MuscleMemoryPower(p, magicNumber), magicNumber));
    }
    
    @Override
    public void upp() {
        upgradeBaseCost(0);
    }
}
