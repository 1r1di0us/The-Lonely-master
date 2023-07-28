package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.AutoplayWaitAction;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;

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
            addToBot(new MakeTempCardInDrawPileAction(new PanicAttack(), 1, false, true));
        addToBot(new AutoplayWaitAction(1.0f));

    }

    public void upp() {
        uDesc();
    }
}
