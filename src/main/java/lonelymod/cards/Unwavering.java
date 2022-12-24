package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.fields.ReturnField;

public class Unwavering extends AbstractEasyCard {
    public final static String ID = makeID("Unwavering");

    public Unwavering() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 12;
        this.isEthereal = true;

    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        int additionalBlock = 0;
        for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mon.hasPower("Strength")) {
                additionalBlock += mon.getPower("Strength").amount;
            }
        }
        this.block += additionalBlock;
        blck();
        ReturnField.willReturn.set(this, true);
    }

    public void upp() {
        upgradeBlock(3);
    }
}
