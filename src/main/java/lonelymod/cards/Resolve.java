package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.ResolvePower;
import lonelymod.powers.SteelResolvePower;

public class Resolve extends AbstractEasyCard {
    public final static String ID = makeID("Resolve");

    public Resolve() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ResolvePower(p, magicNumber), magicNumber));
        if (upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SteelResolvePower(p, 1)));
        }
    }
    
    @Override
    public void onChoseThisOption() { //this happens when you choose this card when playing Lonely
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(this, 1, true, true, false));
    }
    
    @Override
    public void upp() {
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
