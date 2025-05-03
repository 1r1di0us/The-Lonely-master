package lonelymod.cards.colorlesssummons;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.CallMoveAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;

public class CommandDestroy extends AbstractEasyCard {
    public final static String ID = makeID("CommandDestroy");

    public CommandDestroy() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.tags.add(Enums.COMPANION);
        this.selfRetain = true;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new CallMoveAction(AbstractCompanion.ATTACK, CompanionField.currCompanion.get(p)));
    }

    public void upp() {
        this.exhaust = false;
        uDesc();
    }
}