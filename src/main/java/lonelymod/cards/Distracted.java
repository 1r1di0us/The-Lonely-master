package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Distracted extends AbstractEasyCard {
    public final static String ID = makeID("Distracted");

    public Distracted() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.cardsToPreview = new Overreaction();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded)
            addToBot(new MakeTempCardInDrawPileAction(new Overreaction(), 1, false, true));
        else if (upgraded)
            addToBot(new MakeTempCardInHandAction(new Overreaction(), 1));
    }

    public void upp() {
        uDesc();
    }
}
