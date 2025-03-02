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
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        this.exhaust = true;
        // originally, this card starts as innate and plans 20(28) instead of your whole draw pile.
        // I like the original more but the current version might be more palatable to the target audience.
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DiscardDeckAction());
        addToBot(new PlanAction(p.discardPile.size() + p.drawPile.size())); // discard deck action hasn't happened yet.
    }

    public void upp() {
        this.isInnate = true;
        uDesc();
    }
}