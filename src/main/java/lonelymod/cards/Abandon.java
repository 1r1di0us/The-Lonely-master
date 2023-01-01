package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.AbandonAction;

public class Abandon extends AbstractEasyCard {
    public final static String ID = makeID("Abandon");

    public Abandon() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = magicNumber = 2;
        this.baseSecondMagic = secondMagic = 1;
        this.exhaust = true;
        this.isEthereal = true;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, magicNumber));
        }
        else {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, secondMagic));
        }
        AbstractDungeon.actionManager.addToBottom(new AbandonAction());
    }

    public void upp() {
        uDesc();
    }
}
