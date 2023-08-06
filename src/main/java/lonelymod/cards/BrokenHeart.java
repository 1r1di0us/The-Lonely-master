package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import lonelymod.powers.AddCardToHandPower;

public class BrokenHeart extends AbstractEasyCard {
    public final static String ID = makeID("BrokenHeart");

    public BrokenHeart() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.cardsToPreview = new Outburst();
        this.baseMagicNumber = this.magicNumber = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new VulnerablePower(p, this.magicNumber, false), this.magicNumber));
        addToBot(new ApplyPowerAction(p, p, new AddCardToHandPower(p, 1, new Outburst(), true)));
    }

    public void upp() {
        upgradeBaseCost(0);
    }
}
