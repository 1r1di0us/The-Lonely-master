package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import lonelymod.LonelyCharacter;

import static lonelymod.LonelyMod.makeID;

public class CallSpecialAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("CallActionMessage"));
    public static final String[] TEXT = uiStrings.TEXT;

    private AbstractPlayer player;

    public CallSpecialAction() {
        this.actionType = ActionType.SPECIAL;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.player = AbstractDungeon.player;
    }

    public void update() {
        if (LonelyCharacter.currCompanion == null) {
            AbstractDungeon.effectList.add(new ThoughtBubble(this.player.dialogX, this.player.dialogY, 3.0F, TEXT[0], true));
        }
        else {
            LonelyCharacter.currCompanion.callSpecial();
        }
        this.isDone = true;
    }
}