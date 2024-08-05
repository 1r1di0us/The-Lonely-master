package lonelymod.relics;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import lonelymod.LonelyCharacter;
import lonelymod.fields.ReturnField;

import static lonelymod.LonelyMod.makeID;

public class Boomerang extends AbstractEasyRelic {
    public static final String ID = makeID("Boomerang");

    private boolean activated = false;

    public Boomerang() {
        super(ID, RelicTier.RARE, LandingSound.FLAT, LonelyCharacter.Enums.YELLOW);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (!(card.cost == -2) && !this.activated) { //if you are using blue candle or medical kit I'll allow it.
            if (!(card.type == AbstractCard.CardType.POWER) && !card.exhaust) //we will pretend it happened but because it wouldn't do anything we won't do anything either
                ReturnField.willReturn.set(card, true); //NO I DON'T CARE IF YOU HAVE EXHUME, SHUT UP
            flash();
            this.activated = true;
            this.pulse = false;
        }
    }

    @Override
    public void atTurnStart() {
        this.activated = false;
    }
}
