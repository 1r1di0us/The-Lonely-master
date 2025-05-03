package lonelymod.cards.deprecated;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.CallMoveAction;
import lonelymod.actions.ClearThoughtsFollowUpAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;


public class DEPRECATEDClearThoughts extends AbstractEasyCard {
    public final static String ID = makeID("ClearThoughts");

    public DEPRECATEDClearThoughts() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new CallMoveAction(AbstractCompanion.PROTECT, CompanionField.currCompanion.get(AbstractDungeon.player)));
        addToBot(new DrawCardAction(this.magicNumber, new ClearThoughtsFollowUpAction()));
    }

    public void upp() {
        upgradeMagicNumber(1);
    }
}
