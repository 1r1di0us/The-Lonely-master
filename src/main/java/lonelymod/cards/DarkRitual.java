package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RitualPower;

public class DarkRitual extends AbstractEasyCard {
    public final static String ID = makeID("DarkRitual");

    public DarkRitual() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 1;
        this.baseSecondMagic = this.secondMagic = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DiscardAction(p, p, this.secondMagic, true));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new RitualPower(p, this.magicNumber, true), this.magicNumber));
    }

    public void upp() {
        this.isInnate = true;
        uDesc();
    }
}
