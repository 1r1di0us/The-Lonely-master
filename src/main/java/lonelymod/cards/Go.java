package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;

import lonelymod.actions.CompanionAttackAbilityAction;

public class Go extends AbstractEasyCard {
    public final static String ID = makeID("Go");

    public Go() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 0;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            AbstractMonster targetMonster = AbstractDungeon.getRandomMonster();
            addToBot(new ApplyPowerAction(targetMonster, p, new LockOnPower(targetMonster, this.magicNumber), this.magicNumber));
        }
        AbstractDungeon.actionManager.addToBottom(new CompanionAttackAbilityAction());
    }

    public void upp() {
        upgradeMagicNumber(1);
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        this.initializeDescription();
    }
}