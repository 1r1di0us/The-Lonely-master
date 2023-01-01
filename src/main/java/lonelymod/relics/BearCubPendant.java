package lonelymod.relics;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import lonelymod.LonelyCharacter;
import lonelymod.ModFile;

public class BearCubPendant extends AbstractEasyRelic {
    public static final String ID = makeID("BearCubPendant");

    public BearCubPendant() {
        super(ID, RelicTier.BOSS, LandingSound.SOLID, LonelyCharacter.Enums.TODO_COLOR);
        //figured this out from DarkVexon's FishingCharacter
    }    

    @Override
    public void atBattleStart() {
        flash();
    }

    @Override
    public void onEquip() {
        if (AbstractDungeon.player instanceof LonelyCharacter) {
            //((LonelyCharacter) AbstractDungeon.player).onEquipBearCubPendant();
        }
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(WolfPackPendant.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(WolfPackPendant.ID)) {
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
        return AbstractDungeon.player.hasRelic(makeID("WolfPackPendant"));
    }

    @Override
    public String getUpdatedDescription() {
        // Colorize the starter relic's name
        String name = new WolfPackPendant().name;
        StringBuilder sb = new StringBuilder();
        for (String word : name.split(" ")) {
            sb.append("[#").append(ModFile.characterColor.toString()).append("]").append(word).append("[] ");
        }
        sb.setLength(sb.length() - 1);
        sb.append("[#").append(ModFile.characterColor.toString()).append("]");

        return DESCRIPTIONS[0] + sb + DESCRIPTIONS[1];
    }
}
