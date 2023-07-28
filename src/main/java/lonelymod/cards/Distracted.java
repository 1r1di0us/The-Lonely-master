package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.AutoplayWaitAction;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;

public class Distracted extends AbstractEasyCard {
    public final static String ID = makeID("Distracted");

    public Distracted() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.cardsToPreview = new Overreaction();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded)
            addToBot(new MakeTempCardInDrawPileAction(new Overreaction(), 1, true, true));
        else if (upgraded)
            addToBot(new MakeTempCardInDrawPileAction(new Overreaction(), 1, false, true));
        addToBot(new AutoplayWaitAction(1.0f));
    }

    public void upp() {
        uDesc();
    }
}
