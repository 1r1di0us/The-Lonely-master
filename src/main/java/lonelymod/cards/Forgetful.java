package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.AutoplayWaitAction;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;

public class Forgetful extends AbstractEasyCard {
    public final static String ID = makeID("Forgetful");

    public Forgetful() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        AutoplayField.autoplay.set(this, true);
        this.cardsToPreview = (AbstractCard) new PanicAttack();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DiscardAction(p, p, 1, true));
        if (!upgraded)
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard) new PanicAttack(), 1, true, true));
        else if (upgraded)
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard) new PanicAttack(), 1, false, true));
        AbstractDungeon.actionManager.addToBottom(new AutoplayWaitAction(1.0f));

    }

    public void upp() {
        uDesc();
    }
}
