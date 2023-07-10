package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.CallSpecialAction;
import lonelymod.actions.CompanionSpecialAbilityAction;

public class Primal extends AbstractEasyCard {
    public final static String ID = makeID("Primal");

    public Primal() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new CallSpecialAction());
    }

    public void upp() {
        this.exhaust = false;
        uDesc();
    }
}
