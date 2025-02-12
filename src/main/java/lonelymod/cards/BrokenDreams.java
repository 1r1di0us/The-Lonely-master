package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class BrokenDreams extends AbstractEasyCard {
    public final static String ID = makeID("BrokenDreams");

    public BrokenDreams() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.cardsToPreview = new FakeOut();
        this.baseMagicNumber = this.magicNumber = 3;
        this.baseSecondMagic = this.secondMagic = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new MakeTempCardInDrawPileAction(new FakeOut(), 1, true, true));
        addToBot(new DrawCardAction(p, this.secondMagic));
        addToBot(new ApplyPowerAction(p, p, new WeakPower(p, this.magicNumber, false), this.magicNumber));
    }

    public void upp() {
        upgradeSecondMagic(1);
    }
}
