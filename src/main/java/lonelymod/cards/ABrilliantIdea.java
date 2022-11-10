package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.PlanAction;

public class ABrilliantIdea extends AbstractEasyCard {
    public final static String ID = makeID("ABrilliantIdea");

    public ABrilliantIdea() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 4;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new PlanAction(this.magicNumber, this));
    }

    public void upp() {
        upgradeMagicNumber(2);
    }
}