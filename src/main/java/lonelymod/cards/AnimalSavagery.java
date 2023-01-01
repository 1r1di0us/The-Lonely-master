package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.CompanionAttackAbilityAction;
import lonelymod.powers.AnimalSavageryPower;

public class AnimalSavagery extends AbstractEasyCard{
    public final static String ID = makeID("AnimalSavagery");

    public AnimalSavagery() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new CompanionAttackAbilityAction());
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new AnimalSavageryPower(p, this.magicNumber), this.magicNumber));
    }

    public void upp() {
        upgradeMagicNumber(1);
        uDesc();
    }
}
