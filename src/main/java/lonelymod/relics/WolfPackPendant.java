package lonelymod.relics;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import hlysine.friendlymonsters.utils.MinionUtils;
import lonelymod.LonelyCharacter;
import lonelymod.actions.SummonBonesAction;
//import lonelymod.companions.Bones2;

public class WolfPackPendant extends AbstractEasyRelic {
    public static final String ID = makeID("WolfPackPendant");

    public WolfPackPendant() {
        super(ID, RelicTier.STARTER, LandingSound.FLAT, LonelyCharacter.Enums.TODO_COLOR);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new SummonBonesAction(LonelyCharacter.Companions));
        /*MinionUtils.addMinion(AbstractDungeon.player, new Bones2(-750, -25));*/
    }
}
