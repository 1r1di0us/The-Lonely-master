package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;

import lonelymod.actions.AutoplayWaitAction;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;

public class BrokenSpirit extends AbstractEasyCard {
    public final static String ID = makeID("BrokenSpirit");

    public BrokenSpirit() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        AutoplayField.autoplay.set(this, true);
        this.cardsToPreview = (AbstractCard) new Primal();
        this.baseMagicNumber = this.magicNumber = 2;
        this.tags.add(Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FrailPower(p, this.magicNumber, false), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Primal(), 1, true, true));
        AbstractDungeon.actionManager.addToBottom(new AutoplayWaitAction(1.0f));
    }

    public void upp() {
        upgradeBaseCost(0);
    }
}
