package lonelymod.cards.colorlesssummons;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.SummonCompanionAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.cards.colorlesscommands.CommandAttack;
import lonelymod.cards.colorlesscommands.CommandProtect;
import lonelymod.cards.colorlesscommands.CommandSpecial;
import lonelymod.cards.summonmoves.*;
import lonelymod.companions.Bones;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeID;

public class TheDryhound extends AbstractEasyCard {
    public final static String ID = makeID("TheDryhound");


    public TheDryhound() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.exhaust = true;
        this.isInnate = true;
        cardToPreview.addAll(CardTips);
    }

    public void triggerWhenDrawn() {
        addToTop(new DrawCardAction(1));
        addToTop(new SummonCompanionAction(new Bones(), false));
        addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SummonCompanionAction(new Bones(), false));
    }

    public void upp() {
        uDesc();
    }

    public static final ArrayList<AbstractCard> CardTips = new ArrayList<AbstractCard>() {
        {
            add(new Excitement());
            add(new Bark());
            add(new Bite());
            add(new Poise());
            add(new Howl());
        }
    };
}
