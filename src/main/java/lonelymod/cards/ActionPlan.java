package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import lonelymod.actions.CallMoveAction;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;
import lonelymod.powers.AttackNextTurnPower;

public class ActionPlan extends AbstractEasyCard {
    public final static String ID = makeID("ActionPlan");

    public ActionPlan() {
        super(ID, 2, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 10;
        baseMagicNumber = magicNumber = 0;
        //magic Numbers will start out at -1 sometimes if you just say baseMagicNumber = #
        this.tags.add(Enums.COMPANION);
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        addToBot(new CallMoveAction(AbstractCompanion.PROTECT, CompanionField.currCompanion.get(AbstractDungeon.player)));
        addToBot(new ApplyPowerAction(p, p, new AttackNextTurnPower(p, 1)));
        if (upgraded) addToBot(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, this.magicNumber))); //might need my own energized power in the future
    }

    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(1);
        uDesc();
    }
}
