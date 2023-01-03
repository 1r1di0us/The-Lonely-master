package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
//import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.MentalOverloadAction;
import lonelymod.interfaces.TriggerOnHandSizeInterface;

public class MentalOverload extends AbstractEasyCard implements TriggerOnHandSizeInterface {
    public final static String ID = makeID("MentalOverload");

    int baseCost;

    public MentalOverload() {
        super(ID, 9, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseCost = this.cost;
        baseDamage = 24;
        this.isMultiDamage = true; //really?
    }

    /*@Override
    public void triggerWhenDrawn() { //when this card drawn
        addToBot(new MentalOverloadAction(this, baseCost, 1));
    }

    @Override
    public void didDiscard() { //when a card is discarded
        addToBot(new MentalOverloadAction(this, baseCost, 1));
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) { //when a card is played
        addToBot(new MentalOverloadAction(this, baseCost, 1)); //the card is played but its still in your hand right now
    }*/

    public void use(AbstractPlayer p, AbstractMonster m) {
        //addToBot(new MentalOverloadAction(this, baseCost, 1)); // it has not left the hand yet
        allDmg(AttackEffect.SMASH);
    }

    public void upp() {
        upgradeBaseCost(7);
        baseCost = 7;
    }

    public void triggerOnHandSize() {
        addToBot(new MentalOverloadAction(this, baseCost, 1));
    }
}
