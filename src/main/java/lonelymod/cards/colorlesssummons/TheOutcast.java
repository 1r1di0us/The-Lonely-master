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
import lonelymod.companions.Outcast;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeID;

public class TheOutcast extends AbstractEasyCard {
    public final static String ID = makeID("TheOutcast");


    public TheOutcast() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.exhaust = true;
        this.isInnate = true;
        this.cardToPreview.addAll(CardTips);
    }

    public void triggerWhenDrawn() {
        addToTop(new DrawCardAction(1));
        AbstractCard attack = new CommandAttack();
        AbstractCard protect = new CommandProtect();
        AbstractCard special = new CommandSpecial();
        if (upgraded) {
            attack.upgrade();
            protect.upgrade();
            special.upgrade();
        }
        addToTop(new MakeTempCardInDrawPileAction(special, 1, true, true, false));
        addToTop(new MakeTempCardInDrawPileAction(protect, 1, true, true, false));
        addToTop(new MakeTempCardInDrawPileAction(attack, 1, true, true, false));
        addToTop(new SummonCompanionAction(new Outcast(), false));
        addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SummonCompanionAction(new Outcast(), false));
        AbstractCard attack = new CommandAttack();
        AbstractCard protect = new CommandProtect();
        AbstractCard special = new CommandSpecial();
        if (upgraded) {
            attack.upgrade();
            protect.upgrade();
            special.upgrade();
        }
        addToBot(new MakeTempCardInDrawPileAction(attack, 1, true, true, false));
        addToBot(new MakeTempCardInDrawPileAction(protect, 1, true, true, false));
        addToBot(new MakeTempCardInDrawPileAction(special, 1, true, true, false));
        addToBot(new DrawCardAction(1));
    }

    public void upp() {}

    public static final ArrayList<AbstractCard> CardTips = new ArrayList<AbstractCard>() {
        {
            add(new Empower());
            add(new OutcastNothing());
            add(new SpearJab());
            add(new HoldGround());
            add(new TrainUp());
        }
    };
}
