package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.companions.AbstractCompanion;
import com.megacrit.cardcrawl.random.Random;

@SpirePatch(
        clz=AbstractDungeon.class,
        method="generateSeeds"
)

public class SeedCompanionRngPatch {

    public static void Postfix() {
        AbstractCompanion.companionRng = new Random(Settings.seed);
    }
}