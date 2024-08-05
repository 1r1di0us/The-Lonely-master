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
        this.isInnate = true;
        this.exhaust = true;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DiscardDeckAction());
        addToBot(new PlanAction(p.discardPile.size() + p.drawPile.size())); // discard deck action hasn't happened yet.
    }

    public void upp() {
        upgradeBaseCost(1);
    }
}