package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;

public class AbandonAction extends AbstractGameAction {

    @Override
    public void update() {
        if (AbstractDungeon.player.hand.getPurgeableCards().isEmpty()) {
            this.isDone = true;
            return;
        }
        CardGroup TEMP = new CardGroup(AbstractDungeon.player.hand.getPurgeableCards(), CardGroupType.UNSPECIFIED);
        CardGroup REALTEMP = new CardGroup(TEMP, CardGroupType.UNSPECIFIED);
        for (AbstractCard c : TEMP.group) {
            boolean realCard = false;
            for (AbstractCard mc : AbstractDungeon.player.masterDeck.group) {
                if (c.uuid == mc.uuid) {
                    realCard = true;
                    break;
                }
            }
            if (!realCard) {
                REALTEMP.removeCard(c);
            }
        }
        AbstractCard cardToExhaust = AbstractDungeon.player.hand.findCardById(REALTEMP.getRandomCard(AbstractDungeon.cardRandomRng).cardID);
        addToTop(new ExhaustSpecificCardAction(cardToExhaust, AbstractDungeon.player.hand));
        AbstractCard cardToPurge = AbstractDungeon.player.masterDeck.findCardById(cardToExhaust.cardID);
        AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(cardToPurge, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        AbstractDungeon.player.masterDeck.removeCard(cardToPurge);
        this.isDone = true;
    }
    
}
