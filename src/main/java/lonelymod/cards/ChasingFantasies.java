package lonelymod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.CallMoveAction;
import lonelymod.actions.PlanAction;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;

import static lonelymod.LonelyMod.makeID;

public class ChasingFantasies extends AbstractEasyCard {
    public final static String ID = makeID("ChasingFantasies");

    public ChasingFantasies() {
        super(ID, 1, AbstractCard.CardType.SKILL, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
        baseMagicNumber = magicNumber = 4;
        this.shuffleBackIntoDrawPile = true;
        this.tags.add(Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new PlanAction(this.magicNumber));
        addToBot(new CallMoveAction(AbstractCompanion.ATTACK, CompanionField.currCompanion.get(AbstractDungeon.player)));
    }

    public void upp() {
        upgradeMagicNumber(2);
    }
}
