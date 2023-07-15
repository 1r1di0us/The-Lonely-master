package lonelymod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.CallMoveAction;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.ReturnField;

import static lonelymod.LonelyMod.makeID;

public class OnGuard extends AbstractEasyCard {
    public final static String ID = makeID("OnGuard");

    public OnGuard() {
        super(ID, 1, AbstractCard.CardType.SKILL, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        baseBlock = 4;
        this.tags.add(Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo.getIntentBaseDmg() > 0) {
                addToBot(new CallMoveAction(AbstractCompanion.PROTECT));
            }
        }
        ReturnField.willReturn.set(this, true);
    }

    public void upp() {
        upgradeBlock(2);
    }
}
