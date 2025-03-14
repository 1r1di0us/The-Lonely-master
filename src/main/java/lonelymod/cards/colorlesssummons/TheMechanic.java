package lonelymod.cards.colorlesssummons;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.LongerWaitAction;
import lonelymod.actions.SummonMechanicAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.cards.summonmoves.*;
import lonelymod.fields.CompanionField;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeID;

public class TheMechanic extends AbstractEasyCard {
    public final static String ID = makeID("TheMechanic");


    public TheMechanic() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.exhaust = true;
        this.isInnate = true;
        this.cardToPreview.addAll(CardTips);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null)
            CompanionField.currCompanion.set(AbstractDungeon.player, null);
        addToBot(new SummonMechanicAction());
        addToBot(new DrawCardAction(1));
    }

    public void upp() {}

    public static final ArrayList<AbstractCard> CardTips = new ArrayList<AbstractCard>() {
        {
            add(new Power());
            add(new MechanicNothing());
            add(new Electrocute());
            add(new IceWall());
            add(new Supercritical());
        }
    };
}
