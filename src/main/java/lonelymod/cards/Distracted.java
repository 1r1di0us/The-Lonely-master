package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;

public class Distracted extends AbstractEasyCard {
    public final static String ID = makeID("Distracted");

    public Distracted() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        AutoplayField.autoplay.set(this, true);
        MultiCardPreview.add(this, new InstinctiveReaction());
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded)
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard) new InstinctiveReaction(), 1, true, true));
        else if (upgraded)
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard) new InstinctiveReaction(), 1, false, true));
    }

    public void upp() {
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
