package lonelymod.cards.colorlesssummons;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.LongerWaitAction;
import lonelymod.actions.SummonMeatAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.fields.CompanionField;
import lonelymod.fields.ReturnField;

import static lonelymod.LonelyMod.makeID;

public class GodlyPowers extends AbstractEasyCard {
    public final static String ID = makeID("GodlyPowers");

    public GodlyPowers() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.baseMagicNumber = this.magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainEnergyAction(this.magicNumber));
        ReturnField.willReturn.set(this, true);
    }

    public void upp() {
        this.retain = true;
        uDesc();
    }
}
