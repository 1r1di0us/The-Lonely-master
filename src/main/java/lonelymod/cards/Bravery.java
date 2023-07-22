package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.BraveryPower;
import lonelymod.powers.FearlessBraveryPower;
import lonelymod.powers.LonelyPower;

public class Bravery extends AbstractEasyCard {
    public final static String ID = makeID("Bravery");

    private static boolean setCostToZero = false;

    public Bravery() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 5;
        baseSecondMagic = secondMagic = 0;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) addToBot(new ApplyPowerAction(p, p, new FearlessBraveryPower(p, 1, magicNumber, secondMagic)));
        else addToBot(new ApplyPowerAction(p, p, new BraveryPower(p, 1, magicNumber), magicNumber));
    }
    
    @Override
    public void onChoseThisOption() { //this happens when you choose this card when playing Lonely
        this.freeToPlayOnce = true;
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LonelyPower(AbstractDungeon.player, this)));
//        addToBot(new MakeTempCardInDrawPileAction(this, 1, true, true, false));
//        setCostToZero = true;
    }

    /*@Override
    public void triggerWhenDrawn() {
        if (setCostToZero) {
            this.setCostForTurn(0);
            setCostToZero = false;
        }
    }*/

    @Override
    public void upp() {
        upgradeSecondMagic(3);
        uDesc();
    }
}
