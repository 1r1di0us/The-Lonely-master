package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;

import static lonelymod.LonelyMod.makeID;

public class CallMoveAction extends AbstractGameAction {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("CallActionMessage"));
    public static final String[] TEXT = uiStrings.TEXT;

    private AbstractPlayer player;
    private byte move;

    public CallMoveAction(byte move) {
        this.actionType = ActionType.SPECIAL;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
        this.move = move;
    }

    public void update() {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) == null) {
            AbstractDungeon.effectList.add(new ThoughtBubble(this.player.dialogX, this.player.dialogY, 3.0F, TEXT[0], true));
        }
        else {
            if (CompanionField.currCompanion.get(AbstractDungeon.player).nextMove == AbstractCompanion.UNKNOWN) {
                AbstractDungeon.effectList.add(new ThoughtBubble(CompanionField.currCompanion.get(AbstractDungeon.player).dialogX, CompanionField.currCompanion.get(AbstractDungeon.player).dialogY, 3.0F, TEXT[1], false));
            } else if (move == AbstractCompanion.DEFAULT) {
                CompanionField.currCompanion.get(AbstractDungeon.player).callDefault();
            } else if (move == AbstractCompanion.ATTACK) {
                CompanionField.currCompanion.get(AbstractDungeon.player).callAttack();
            } else if (move == AbstractCompanion.PROTECT) {
                CompanionField.currCompanion.get(AbstractDungeon.player).callProtect();
            } else if (move == AbstractCompanion.SPECIAL) {
                CompanionField.currCompanion.get(AbstractDungeon.player).callSpecial();
            } else if (move == AbstractCompanion.UNKNOWN) {
                CompanionField.currCompanion.get(AbstractDungeon.player).callUnknown();
            } else if (move == AbstractCompanion.NONE) {
                CompanionField.currCompanion.get(AbstractDungeon.player).callNone();
            } else {
                AbstractDungeon.effectList.add(new ThoughtBubble(this.player.dialogX, this.player.dialogY, 3.0F, TEXT[2], true));
            }
        }
        this.isDone = true;
    }
}