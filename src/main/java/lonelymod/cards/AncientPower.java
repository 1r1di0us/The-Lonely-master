package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.AncientPowerPower;

public class AncientPower extends AbstractEasyCard {
    public final static String ID = makeID("AncientPower");

    public AncientPower() {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        this.isEthereal = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new AncientPowerPower(p)));
    }
    
    public void upp() {
        this.isEthereal = false;
        uDesc();
    }
}
