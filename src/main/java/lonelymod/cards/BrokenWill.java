package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import lonelymod.actions.AutoplayWaitAction;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;

public class BrokenWill extends AbstractEasyCard {
    public final static String ID = makeID("BrokenWill");

    public BrokenWill() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        AutoplayField.autoplay.set(this, true);
        this.cardsToPreview = (AbstractCard) new FeignWeakness();
        this.baseMagicNumber = this.magicNumber = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new WeakPower(p, this.magicNumber, false), this.magicNumber));
        if (!upgraded)
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard) new FeignWeakness(), 1, true, true));
        else if (upgraded)
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard) new FeignWeakness(), 1, false, true));
        AbstractDungeon.actionManager.addToBottom(new AutoplayWaitAction(1.0f));
        }

    public void upp() {
        uDesc();
    }
}
