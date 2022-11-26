package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.FearOfTheBeastPower;

public class FearOfTheBeast extends AbstractEasyCard {
    public final static String ID = makeID("FearOfTheBeast");

    public FearOfTheBeast() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FearOfTheBeastPower(p, magicNumber), magicNumber));
    }
    
    public void upp() {
        upgradeMagicNumber(1);
    }
}
