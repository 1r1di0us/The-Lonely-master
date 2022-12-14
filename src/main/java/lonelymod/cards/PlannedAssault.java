package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.GashAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PlannedAssault extends AbstractEasyCard {
    public final static String ID = makeID("PlannedAssault");

    public PlannedAssault() {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 2;
        baseMagicNumber = magicNumber = 5;
        this.exhaust = true;
    }
    
    @Override
    public void triggerWhenDrawn() {
        AbstractDungeon.actionManager.addToBottom(new GashAction(this, magicNumber));
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
    }

    public void upp() {
        upgradeMagicNumber(3);
    }
}
