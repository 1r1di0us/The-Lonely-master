package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.DefensiveReflexesPower;

public class DefensiveReflexes extends AbstractEasyCard {
    public final static String ID = makeID("DefensiveReflexes");

    public DefensiveReflexes() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 4;
        baseSecondMagic = secondMagic = 12; //we don't actually use this except for the description
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DefensiveReflexesPower(p, magicNumber), magicNumber));
    }
    
    public void upp() {
        this.isInnate = true;
        uDesc();
    }
}
