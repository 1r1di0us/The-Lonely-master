package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.fields.CompanionField;

public class KillCompanionAction extends AbstractGameAction {

    public void update() {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            CompanionField.currCompanion.set(AbstractDungeon.player, null);
        }
        this.isDone = true;
    }
}
