package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.FearTheBeastPower;

public class FearTheBeast extends AbstractEasyCard {
    public final static String ID = makeID("FearTheBeast");

    public FearTheBeast() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 5;
        this.tags.add(Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new FearTheBeastPower(p, magicNumber), magicNumber));
    }
    
    public void upp() {
        upgradeMagicNumber(2);
    }
}
