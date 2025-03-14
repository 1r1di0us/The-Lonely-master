package lonelymod.cards.colorlesssummons;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.SummonOmenAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.cards.summonmoves.*;
import lonelymod.fields.CompanionField;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeID;

public class TheVulchyr extends AbstractEasyCard {
    public final static String ID = makeID("TheVulchyr");


    public TheVulchyr() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        this.exhaust = true;
        this.isInnate = true;
        this.cardToPreview.addAll(CardTips);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null)
            CompanionField.currCompanion.set(AbstractDungeon.player, null);
        addToBot(new SummonOmenAction(true));
        addToBot(new DrawCardAction(1));
    }

    public void upp() {}

    public static final ArrayList<AbstractCard> CardTips = new ArrayList<AbstractCard>() {
        {
            add(new Claws());
            add(new Peck());
            add(new Shred());
            add(new Shriek());
            add(new Sharpen());
        }
    };
}
