package lonelymod.relics;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.LonelyCharacter;
import lonelymod.cards.colorlesssummons.GodlyPowers;

import static lonelymod.LonelyMod.makeID;

public class GiftOfGod extends AbstractEasyRelic {
    public static final String ID = makeID("GiftOfGod");

    public GiftOfGod() {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL, LonelyCharacter.Enums.YELLOW);
        cardToPreview.add(new GodlyPowers());
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public void atBattleStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new MakeTempCardInDrawPileAction(new GodlyPowers(), 1, true, true));
    }
}
