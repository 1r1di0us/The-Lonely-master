package lonelymod.cards.colorlesssummons;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.fields.CompanionField;
import lonelymod.fields.ReturnField;

import static lonelymod.LonelyMod.makeID;

public class FeedTheBear extends AbstractEasyCard {
    public final static String ID = makeID("FeedTheBear");

    private static final UIStrings uistring = CardCrawlGame.languagePack.getUIString(makeID("FeedTheBearMessage"));
    public static final String[] TEXT = uistring.TEXT;

    public FeedTheBear() {
        super(ID, 1, CardType.SKILL, CardRarity.SPECIAL, CardTarget.ENEMY, CardColor.COLORLESS);
        this.exhaust = true;
        this.isEthereal = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        } else if (m == null) {
            return false;
        } else if (m.isDeadOrEscaped()) {
            return false;
        } else if (CompanionField.currCompanion.get(p) != m) {
            canUse = false;
            this.cantUseMessage = TEXT[0];
        }
        return canUse;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        return;
    }

    public void upp() {
        return;
    }
}
