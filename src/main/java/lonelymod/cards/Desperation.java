package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.DesperationPower;
import lonelymod.powers.LonelyPower;
import lonelymod.powers.UtterDesperationPower;

public class Desperation extends AbstractEasyCard {
    public final static String ID = makeID("Desperation");

    public Desperation() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) addToBot(new ApplyPowerAction(p, p, new UtterDesperationPower(p, magicNumber)));
        else addToBot(new ApplyPowerAction(p, p, new DesperationPower(p, magicNumber)));
    }
    
    @Override
    public void onChoseThisOption() { //this happens when you choose this card when playing Lonely
        this.freeToPlayOnce = true;
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LonelyPower(AbstractDungeon.player, this)));
        //addToBot(new MakeTempCardInDrawPileAction(this, 1, true, true, false));
        //setCostToZero = true;
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
        uDesc();
    }
}
