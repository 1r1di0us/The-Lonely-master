package lonelymod.relics;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import lonelymod.LonelyCharacter;
import lonelymod.LonelyMod;
import lonelymod.actions.SummonMeatAction;

public class MeatsStomach extends AbstractEasyRelic {
    public static final String ID = makeID("MeatsStomach");

    public MeatsStomach() {
        super(ID, RelicTier.BOSS, LandingSound.SOLID, LonelyCharacter.Enums.TODO_COLOR);
        //figured this out from DarkVexon's FishingCharacter
    }    

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new SummonMeatAction());
    }

    @Override
    public void onEquip() {
        if (AbstractDungeon.player instanceof LonelyCharacter) {
            ((LonelyCharacter) AbstractDungeon.player).onEquipMeatsStomach();
        }
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(BonesStomach.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(BonesStomach.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(BonesStomach.ID);
    }

    @Override
    public String getUpdatedDescription() {
        // Colorize the starter relic's name
        String name = new BonesStomach().name;
        StringBuilder sb = new StringBuilder();
        for (String word : name.split(" ")) {
            sb.append("[#").append(LonelyMod.characterColor.toString()).append("]").append(word).append("[] ");
        }
        sb.setLength(sb.length() - 1);
        sb.append("[#").append(LonelyMod.characterColor.toString()).append("]");

        return DESCRIPTIONS[0] + sb + DESCRIPTIONS[1];
    }
}
