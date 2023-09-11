package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.ReturnAllCardsAction;
import lonelymod.actions.ReturnToHandAction;
import lonelymod.fields.ReturnField;
import lonelymod.interfaces.TriggerOnReturnInterface;

public class Cycle extends AbstractEasyCard {
    public final static String ID = makeID("Cycle");

    public Cycle() {
        super(ID, 0, CardType.ATTACK, CardRarity.RARE, CardTarget.SELF_AND_ENEMY);
        baseDamage = 6;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        addToBot(new ReturnAllCardsAction(false));
    }

    public void upp() {
        upgradeDamage(3);
    }
}
