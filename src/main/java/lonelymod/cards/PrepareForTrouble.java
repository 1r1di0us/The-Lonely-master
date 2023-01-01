package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.CompanionProtectAbilityAction;
import lonelymod.powers.StaminaPower;

public class PrepareForTrouble extends AbstractEasyCard {
    public final static String ID = makeID("PrepareForTrouble");

    public PrepareForTrouble() {
        super(ID, 2, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 2;
        //magic Numbers will start out at -1 sometimes if you just say baseMagicNumber = #
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StaminaPower(p, this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.secondMagic));
        if (upgraded) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
        }
        AbstractDungeon.actionManager.addToBottom(new CompanionProtectAbilityAction());
    }

    public void upp() {
        upgradeMagicNumber(1);
        uDesc();
    }
}
