package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.ReturnToHandAction;
import lonelymod.fields.ReturnField;

public class Cycle extends AbstractEasyCard {
    public final static String ID = makeID("Cycle");

    public Cycle() {
        super(ID, 0, CardType.ATTACK, CardRarity.RARE, CardTarget.SELF);
        baseDamage = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (ReturnField.willReturn.get(c)) {
                //ReturnField.willReturn.set(c, false);
                AbstractDungeon.actionManager.addToBottom(new ReturnToHandAction(c));
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (ReturnField.willReturn.get(c)) {
                //ReturnField.willReturn.set(c, false);
                AbstractDungeon.actionManager.addToBottom(new ReturnToHandAction(c));
            }
        }
    }

    public void upp() {
        upgradeDamage(3);
    }
}
