package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Forgetful extends AbstractEasyCard {
    public final static String ID = makeID("Forgetful");

    public Forgetful() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        this.cardsToPreview = new PanicAttack();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DiscardAction(p, p, 1, true));
        if (!upgraded)
            addToBot(new MakeTempCardInDrawPileAction(new PanicAttack(), 1, true, true));
        else if (upgraded)
            addToBot(new MakeTempCardInDrawPileAction(new PanicAttack(), 1, false, false));
    }

    public void upp() {
        uDesc();
    }
}
