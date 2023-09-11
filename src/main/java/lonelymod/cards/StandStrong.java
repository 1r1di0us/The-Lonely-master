package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.fields.ReturnField;

public class StandStrong extends AbstractEasyCard {
    public final static String ID = makeID("StandStrong");

    public StandStrong() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 7;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        ReturnField.willReturn.set(this, true);
    }

    public void upp() {
        upgradeBlock(3);
    }
}