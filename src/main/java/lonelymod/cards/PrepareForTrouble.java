package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import lonelymod.orbs.WolfProtectAction;
import lonelymod.powers.StaminaPower;

public class PrepareForTrouble extends AbstractEasyCard {
    public final static String ID = makeID("PrepareForTrouble");

    public PrepareForTrouble() {
        super(ID, 2, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = 3;
        baseSecondMagic = 2;
        this.magicNumber = this.baseMagicNumber;
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StaminaPower(p, this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.secondMagic));
        if (upgraded) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
        }
        AbstractDungeon.player.channelOrb((AbstractOrb) new WolfProtectAction());
    }

    public void upp() {
        upgradeMagicNumber(1);
    }
}
