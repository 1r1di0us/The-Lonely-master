package lonelymod.cards.colorlesssummons;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;
import lonelymod.powers.CompanionVigorPower;

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

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null && CompanionField.currCompanion.get(AbstractDungeon.player) == mo) {
            byte move = mo.nextMove;
            switch (move) {
                case AbstractCompanion.DEFAULT:
                    this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
                    this.initializeDescription();
                    break;
                case AbstractCompanion.ATTACK:
                    if (!CompanionField.currCompanion.get(AbstractDungeon.player).hasPower(CompanionVigorPower.POWER_ID)) {
                        this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[4];
                    } else {
                        this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[1];
                    }
                    this.initializeDescription();
                    break;
                case AbstractCompanion.PROTECT:
                    this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[2];
                    this.initializeDescription();
                    break;
                case AbstractCompanion.SPECIAL:
                    this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[3];
                    this.initializeDescription();
                    break;
                default:
                    this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[4];
                    this.initializeDescription();
                    break;
            }
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        if (!cardStrings.DESCRIPTION.equals(this.rawDescription)) {
            this.rawDescription = cardStrings.DESCRIPTION;
            initializeDescription();
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        return;
    }

    public void upp() {
        return;
    }
}
