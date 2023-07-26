package lonelymod.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import lonelymod.LonelyCharacter;
import lonelymod.actions.ToyCatAction;
import lonelymod.fields.InToyCatField;

import static lonelymod.LonelyMod.makeID;

public class ToyCat {} /*extends AbstractEasyRelic {
    public static final String ID = makeID("ToyCat");

    private boolean cardSelected = true;
    public AbstractCard card = null;
    private static AbstractCard cardCopy = null;

    public ToyCat() {
        super(ID, RelicTier.SHOP, LandingSound.MAGICAL, LonelyCharacter.Enums.YELLOW);
    }

    public AbstractCard getCard() {
        return this.card.makeCopy();
    }

    public static AbstractCard getCardCopy() {
        return cardCopy.makeCopy();
    }

    public void onEquip() {
        CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if ((c.type == AbstractCard.CardType.ATTACK || c.type == AbstractCard.CardType.SKILL) && !c.exhaust) {
                cards.addToBottom(c);
            }
        }
        if (cards.getPurgeableCards().size() > 0) {
            this.cardSelected = false;
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }
            (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.INCOMPLETE;
            AbstractDungeon.gridSelectScreen.open(cards.getPurgeableCards(), 1, this.DESCRIPTIONS[1] + this.name + LocalizedStrings.PERIOD, false, false, false, false);
        }
    }

    public void onUnequip() {
        if (this.card != null) {
            AbstractCard cardInDeck = AbstractDungeon.player.masterDeck.getSpecificCard(this.card);
            if (cardInDeck != null)
                InToyCatField.inToyCat.set(cardInDeck, false);
        }
    }

    public void update() {
        super.update();
        if (!this.cardSelected &&
                !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            this.cardSelected = true;
            this.card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            this.cardCopy = card.makeStatEquivalentCopy();
            InToyCatField.inToyCat.set(this.card, true);
            (AbstractDungeon.getCurrRoom()).phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.description = this.DESCRIPTIONS[2] + FontHelper.colorString(this.card.name, "y") + this.DESCRIPTIONS[3];
            this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
            initializeTips();
        }
    }

    public void setDescriptionAfterLoading() {
        this.description = this.DESCRIPTIONS[2] + FontHelper.colorString(this.card.name, "y") + this.DESCRIPTIONS[3];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    public void atTurnStart() {
        flash();
        addToTop(new ToyCatAction(this.card));
    }

    public boolean canSpawn() {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if ((c.type == AbstractCard.CardType.ATTACK || c.type == AbstractCard.CardType.SKILL) && c.rarity != AbstractCard.CardRarity.BASIC && !c.exhaust)
                return true;
        }
        return false;
    }
}*/
