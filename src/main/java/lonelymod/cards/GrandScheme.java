package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.DiscardDeckAction;
import lonelymod.actions.PlanAction;

public class GrandScheme extends AbstractEasyCard {
    public final static String ID = makeID("GrandScheme");

    public GrandScheme() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseMagicNumber = magicNumber = 20;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DiscardDeckAction());
        AbstractDungeon.actionManager.addToBottom(new PlanAction(this.magicNumber));
    }

    public void upp() {
        upgradeMagicNumber(8);
    }
}