package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.fields.ReturnField;

public class Unwavering extends AbstractEasyCard {
    public final static String ID = makeID("Unwavering");

    public Unwavering() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 10;
        this.baseMagicNumber = this.magicNumber = 10;
        //magic number just sets the thing in the description
        this.isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        ReturnField.willReturn.set(this, true);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBaseBlock = this.baseBlock;
        for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mon.hasPower("Strength")) {
                this.baseBlock += mon.getPower("Strength").amount;
            }
        }
        super.calculateCardDamage(mo);
        this.baseBlock = realBaseBlock;
        this.isBlockModified = (this.block != this.baseBlock);
    }

    @Override
    public void applyPowers() {
        int realBaseBlock = this.baseBlock;
        for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mon.hasPower("Strength")) {
                this.baseBlock += mon.getPower("Strength").amount;
            }
        }
        super.applyPowers();
        this.baseBlock = realBaseBlock;
        this.isBlockModified = (this.block != this.baseBlock);
    }

    public void upp() {
        upgradeBlock(3);
        upgradeMagicNumber(3);
    }
}
