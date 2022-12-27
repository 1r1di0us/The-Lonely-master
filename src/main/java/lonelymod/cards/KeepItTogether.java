package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.ReturnToHandAction;
import lonelymod.fields.ReturnField;

public class KeepItTogether extends AbstractEasyCard {
    public final static String ID = makeID("KeepItTogether");

    public KeepItTogether() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));
        if (upgraded) {
            ReturnField.willReturn.set(this, true);
        }
    }

    @Override
    public void triggerOnManualDiscard() {
        if (!AbstractDungeon.player.hasPower("AncientPowerPower")) //card will be exhausted if we have this power so its no use
            AbstractDungeon.actionManager.addToBottom(new ReturnToHandAction(this));
    }

    public void upp() {
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }
}
