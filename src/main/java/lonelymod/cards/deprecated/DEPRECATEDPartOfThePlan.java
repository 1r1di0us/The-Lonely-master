package lonelymod.cards.deprecated;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.DrawToHandAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.interfaces.TriggerOnPlanInterface;

public class DEPRECATEDPartOfThePlan extends AbstractEasyCard implements TriggerOnPlanInterface {
    public final static String ID = makeID("PartOfThePlan");

    public DEPRECATEDPartOfThePlan() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
    }

    @Override
    public void triggerOnPlan(boolean inPlanSelect) {
        if (AbstractDungeon.player.drawPile.contains(this) && !inPlanSelect) {
            addToBot(new DrawToHandAction(this));
        }
    }

    public void upp() {
        upgradeBlock(1);
    }
}