package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.CompanionSpecialAbilityAction;

public class PrimalInstinct extends AbstractEasyCard {
    public final static String ID = makeID("PrimalInstinct");

    public PrimalInstinct() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new CompanionSpecialAbilityAction());
    }

    public void upp() {
        this.exhaust = false;
        uDesc();
    }
}
