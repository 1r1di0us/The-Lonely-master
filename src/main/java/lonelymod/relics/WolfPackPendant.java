package lonelymod.relics;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import lonelymod.LonelyCharacter;
import lonelymod.orbs.WolfAttackAction;
import lonelymod.orbs.WolfNormalAction;

public class WolfPackPendant extends AbstractEasyRelic {
    public static final String ID = makeID("WolfPackPendant");

    public WolfPackPendant() {
        super(ID, RelicTier.STARTER, LandingSound.FLAT, LonelyCharacter.Enums.TODO_COLOR);
    }

    @Override
    public void atTurnStart() {
        flash();
        if (AbstractDungeon.player.hasPower(makeID("WildFormPower"))) {
            AbstractDungeon.player.channelOrb((AbstractOrb) new WolfAttackAction());
        } else {
            AbstractDungeon.player.channelOrb((AbstractOrb) new WolfNormalAction());
        }
    }
}
