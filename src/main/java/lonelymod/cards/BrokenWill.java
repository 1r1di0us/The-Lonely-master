package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;

public class BrokenWill extends AbstractEasyCard {
    public final static String ID = makeID("BrokenWill");

    public BrokenWill() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        AutoplayField.autoplay.set(this, true);
        MultiCardPreview.add(this, new FeignWeakness());
        this.baseMagicNumber = this.magicNumber = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new WeakPower(p, this.magicNumber, false), this.magicNumber));
        if (!upgraded)
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard) new FeignWeakness(), 1, true, true));
        else if (upgraded)
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard) new FeignWeakness(), 1, false, true));
    }

    public void upp() {
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}