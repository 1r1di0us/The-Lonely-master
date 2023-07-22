package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PlayCardAction extends AbstractGameAction {
    private boolean exhaustCards;
    private AbstractCard cardToPlay;
    private CardGroup group;

    public PlayCardAction(CardGroup group, AbstractCard card, AbstractCreature target, boolean exhausts) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = AbstractGameAction.ActionType.WAIT;
        this.source = (AbstractCreature)AbstractDungeon.player;
        this.group = group;
        this.cardToPlay = card;
        this.target = target;
        this.exhaustCards = exhausts;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (group.isEmpty()) {
                this.isDone = true;
                return;
            }
            if (!group.isEmpty()) {
                group.group.remove(cardToPlay);
                (AbstractDungeon.getCurrRoom()).souls.remove(cardToPlay);
                cardToPlay.exhaustOnUseOnce = this.exhaustCards;
                AbstractDungeon.player.limbo.group.add(cardToPlay);
                cardToPlay.current_y = -200.0F * Settings.scale;
                cardToPlay.target_x = Settings.WIDTH / 2.0F + 200.0F * Settings.xScale;
                cardToPlay.target_y = Settings.HEIGHT / 2.0F;
                cardToPlay.targetAngle = 0.0F;
                cardToPlay.lighten(false);
                cardToPlay.drawScale = 0.12F;
                cardToPlay.targetDrawScale = 0.75F;
                cardToPlay.applyPowers();
                addToTop((AbstractGameAction)new NewQueueCardAction(cardToPlay, this.target, false, true));
                addToTop((AbstractGameAction)new UnlimboAction(cardToPlay));
                if (!Settings.FAST_MODE) {
                    addToTop((AbstractGameAction)new WaitAction(Settings.ACTION_DUR_MED));
                } else {
                    addToTop((AbstractGameAction)new WaitAction(Settings.ACTION_DUR_FASTER));
                }
            }
            this.isDone = true;
        }
    }
}
