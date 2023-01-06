package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class AccursedBeakAction extends AbstractGameAction {
    private AbstractPlayer player;
    private AbstractMonster target;
    private AbstractCard cardToQueue;

    public AccursedBeakAction(AbstractPlayer player, AbstractMonster target, AbstractCard cardToQueue) {
        this.player = player;
        this.target = target;
        this.cardToQueue = cardToQueue;
    }

    @Override
    public void update() {
        if (player.hasPower("Strength")) {
            if (player.getPower("Strength").amount > 0) {
                AbstractCard tmp = cardToQueue.makeStatEquivalentCopy();
                tmp.purgeOnUse = true;
                addToBot(new NewQueueCardAction(tmp, target, false, true));
            }
        }
    }

}
