package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;

public class Forgetful extends AbstractEasyCard {
    public final static String ID = makeID("Forgetful");

    public Forgetful() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        AutoplayField.autoplay.set(this, true);
        MultiCardPreview.add(this, new PanicAttack());
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DiscardAction(p, p, 1, true));
        if (!upgraded)
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard) new PanicAttack(), 1, true, true));
        else if (upgraded)
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard) new PanicAttack(), 1, false, true));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(Settings.ACTION_DUR_MED)); //0.5f
    }

    public void upp() {
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
