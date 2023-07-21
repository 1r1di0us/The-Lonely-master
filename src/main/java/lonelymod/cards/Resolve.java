package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.ResolvePower;
import lonelymod.powers.SteelResolvePower;

public class Resolve extends AbstractEasyCard {
    public final static String ID = makeID("Resolve");

    private static boolean setCostToZero = false;

    public Resolve() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 6;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) addToBot(new ApplyPowerAction(p, p, new SteelResolvePower(p, 1, this.magicNumber)));
        else addToBot(new ApplyPowerAction(p, p, new ResolvePower(p, 1, magicNumber)));
    }
    
    @Override
    public void onChoseThisOption() { //this happens when you choose this card when playing Lonely
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(this, 1, true, true, false));
        setCostToZero = true;
    }

    @Override
    public void triggerWhenDrawn() {
        if (setCostToZero) {
            this.setCostForTurn(0);
            setCostToZero = false;
        }
    }
    
    @Override
    public void upp() {
        uDesc();
    }
}
