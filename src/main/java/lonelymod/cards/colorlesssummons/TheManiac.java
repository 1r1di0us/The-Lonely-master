package lonelymod.cards.colorlesssummons;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.AutoplayWaitAction;
import lonelymod.actions.SummonManiacAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.fields.CompanionField;

import static lonelymod.LonelyMod.makeID;

public class TheManiac extends AbstractEasyCard {
    public final static String ID = makeID("TheManiac");


    public TheManiac() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.baseMagicNumber = this.magicNumber = 1;
        this.exhaust = true;
        this.isInnate = true;
        AutoplayField.autoplay.set(this, true);
        //this.tags.add(Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AutoplayWaitAction(1.0f));
        addToBot(new DrawCardAction(p, magicNumber));
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null)
            CompanionField.currCompanion.set(AbstractDungeon.player, null);
        addToBot(new SummonManiacAction());
    }

    public void upp() {

    }
}
