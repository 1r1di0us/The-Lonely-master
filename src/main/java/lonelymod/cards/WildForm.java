package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;

import lonelymod.powers.WildFormPower;

public class WildForm extends AbstractEasyCard {
    public final static String ID = makeID("WildForm");

    public WildForm() {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        //tags.add(CardTags.FORM); NOOOO WHY WOULD YOU TAKE THIS FROM ME!?
        baseMagicNumber = magicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new WildFormPower(p, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FocusPower(p, magicNumber), magicNumber));
    }
    
    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }
}
