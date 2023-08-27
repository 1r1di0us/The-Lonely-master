package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.DrawToHandAction;
import lonelymod.interfaces.TriggerOnPlanInterface;

public class PlannedAssault extends AbstractEasyCard implements TriggerOnPlanInterface {
    public final static String ID = makeID("PlannedAssault");

    public PlannedAssault() {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 4;
    }
    
    @Override
    public void triggerOnPlan(boolean thisCardPlanned) {
        if (AbstractDungeon.player.drawPile.contains(this) && !thisCardPlanned) { //triggerOnPlan triggers twice for cards that were planned technically
            addToBot(new DrawToHandAction(this));
        }
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
    }

    public void upp() {
        upgradeDamage(2);
    }
}
