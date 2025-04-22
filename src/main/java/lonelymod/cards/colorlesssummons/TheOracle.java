package lonelymod.cards.colorlesssummons;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.SummonOracleAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.cards.summonmoves.*;
import lonelymod.fields.CompanionField;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeID;

public class TheOracle extends AbstractEasyCard {
    public final static String ID = makeID("TheOracle");


    public TheOracle() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.exhaust = true;
        this.isInnate = true;
        this.cardToPreview.addAll(CardTips);
    }

    public void triggerWhenDrawn() {
        addToTop(new DrawCardAction(1));
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null)
            CompanionField.currCompanion.set(AbstractDungeon.player, null);
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
        addToTop(new SummonOracleAction(true));
        addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null)
            CompanionField.currCompanion.set(AbstractDungeon.player, null);
        addToBot(new SummonOracleAction(true));
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
            add(new Charge());
            add(new OracleNothing());
            add(new FieryBlast());
            add(new PeacefulProtest());
            add(new Blessing());
        }
    };
}
