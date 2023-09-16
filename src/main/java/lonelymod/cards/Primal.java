package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.CallMoveAction;
import lonelymod.actions.CompanionTakeTurnAction;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;

public class Primal extends AbstractEasyCard {
    public final static String ID = makeID("Primal");

    public Primal() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
        this.tags.add(Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) addToBot(new CompanionTakeTurnAction(false));
        addToBot(new CallMoveAction(AbstractCompanion.SPECIAL, CompanionField.currCompanion.get(AbstractDungeon.player)));
    }

    public void upp() {
        uDesc();
    }
}
