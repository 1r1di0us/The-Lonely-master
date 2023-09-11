package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.OutburstAction;

public class Outburst extends AbstractEasyCard {
    public final static String ID = makeID("Outburst");

    public Outburst() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        this.exhaust = true;
        baseMagicNumber = magicNumber = 3; //I would have loved it if it was 4 but then broken heart would just be unusable.
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new OutburstAction(magicNumber, this.upgraded));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean retVal = false;
        for (AbstractCard c : p.discardPile.group) {
            if (c.type == CardType.ATTACK) {
                retVal = true;
            }
        }
        return retVal;
    }

    public void upp() {
        uDesc();
    }
}
