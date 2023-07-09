package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Overreaction extends AbstractEasyCard {
    public final static String ID = makeID("Overreaction");

    public Overreaction() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 6;
        baseMagicNumber = magicNumber = 1;
        baseSecondMagic = secondMagic = 1;
        exhaust = true;
        isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, magicNumber));
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(secondMagic));
    }

    public void upp() {
        upgradeBlock(2);
        upgradeSecondMagic(1);
    }
}
