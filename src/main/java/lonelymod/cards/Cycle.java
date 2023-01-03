package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.ReturnToHandAction;
import lonelymod.fields.ReturnField;

public class Cycle extends AbstractEasyCard {
    public final static String ID = makeID("Cycle");

    public Cycle() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
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
        upgradeBaseCost(0);
    }
}
