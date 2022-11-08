package lonelymod.relics;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import lonelymod.LonelyCharacter;
import lonelymod.orbs.WolfNormalAction;

public class WolfPackPendant extends AbstractEasyRelic {
    public static final String ID = makeID("WolfPackPendant");

    public WolfPackPendant() {
        super(ID, RelicTier.STARTER, LandingSound.FLAT, LonelyCharacter.Enums.TODO_COLOR);
    }

    public void atTurnStart() {
        flash();
        AbstractDungeon.player.channelOrb((AbstractOrb) new WolfNormalAction());
    }
}
