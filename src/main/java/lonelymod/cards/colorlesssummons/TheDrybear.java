package lonelymod.cards.colorlesssummons;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.SummonMeatAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.cards.summonmoves.*;
import lonelymod.fields.CompanionField;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeID;

public class TheDrybear extends AbstractEasyCard {
    public final static String ID = makeID("TheDrybear");


    public TheDrybear() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.exhaust = true;
        this.isInnate = true;
        cardToPreview.addAll(CardTips);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null)
            CompanionField.currCompanion.set(AbstractDungeon.player, null);
        addToBot(new SummonMeatAction());
        addToBot(new DrawCardAction(1));
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
    }

    public void upp() {

    }

    public static final ArrayList<AbstractCard> CardTips = new ArrayList<AbstractCard>() {
        {
            add(new EnergyStore());
            add(new Stretch());
            add(new Maul());
            add(new BodyBlock());
            add(new Devour());
        }
    };
}
