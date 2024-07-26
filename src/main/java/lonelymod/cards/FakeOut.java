package lonelymod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.FakeOutAction;

import static lonelymod.LonelyMod.makeID;

public class FakeOut extends AbstractEasyCard {
    public final static String ID = makeID("FakeOut");

    public FakeOut() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 1;
        baseBlock = 12;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded)
            addToBot(new FakeOutAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), this.block));
        else {
            dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            blck();
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        if (!upgraded) {
            if (mo.currentBlock < this.damage) {
                this.baseMagicNumber = this.magicNumber = this.block - (this.damage - mo.currentBlock);
            }
            else if (this.block - (this.damage - mo.currentBlock) <= 0) {
                this.baseMagicNumber = this.magicNumber = 0;
            } else {
                this.baseMagicNumber = this.magicNumber = this.block;
            }
            this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
            this.initializeDescription();
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        if (!cardStrings.DESCRIPTION.equals(this.rawDescription) && !upgraded) {
            this.rawDescription = cardStrings.DESCRIPTION;
            initializeDescription();
        }
    }

    public void onMoveToDiscard() {
        if (!upgraded) {
            this.rawDescription = cardStrings.DESCRIPTION;
            initializeDescription();
        }
    }

    public void upp() {
        upgradeBlock(3);
        uDesc();
    }
}
