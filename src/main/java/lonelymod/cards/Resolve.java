package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.LonelyPower;
import lonelymod.powers.ResolvePower;
import lonelymod.powers.SteelResolvePower;

public class Resolve extends AbstractEasyCard {
    public final static String ID = makeID("Resolve");

    public Resolve() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 0;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) addToBot(new ApplyPowerAction(p, p, new SteelResolvePower(p, 1, this.magicNumber, this.secondMagic)));
        else addToBot(new ApplyPowerAction(p, p, new ResolvePower(p, 1, magicNumber)));
    }
    
    @Override
    public void onChoseThisOption() { //this happens when you choose this card when playing Lonely
        this.freeToPlayOnce = true;
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LonelyPower(AbstractDungeon.player, this)));
    }
    
    @Override
    public void upp() {
        upgradeSecondMagic(3);
        uDesc();
    }
}
