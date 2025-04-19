package lonelymod.cards.summonmoves;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.CallMoveAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;

public class CommandSpecial extends AbstractEasyCard {
    public final static String ID = makeID("CommandSpecial");

    public CommandSpecial() {
        super(ID, 1, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF);
        exhaust = true;
        isEthereal = true;
        this.tags.add(Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new CallMoveAction(AbstractCompanion.SPECIAL, CompanionField.currCompanion.get(AbstractDungeon.player)));
    }

    public void upp() {
        uDesc();
        this.isEthereal = false;
        this.exhaust = false;
    }
}
