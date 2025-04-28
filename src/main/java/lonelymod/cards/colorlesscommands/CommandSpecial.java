package lonelymod.cards.colorlesscommands;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.CallMoveAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;

public class CommandSpecial extends AbstractEasyCard {
    public final static String ID = makeID("CommandSpecial");

    public CommandSpecial() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF, CardColor.COLORLESS);
        exhaust = true;
        this.tags.add(Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new CallMoveAction(AbstractCompanion.SPECIAL, CompanionField.currCompanion.get(p)));
    }

    public void upp() {
        upgradeBaseCost(1);
    }
}
