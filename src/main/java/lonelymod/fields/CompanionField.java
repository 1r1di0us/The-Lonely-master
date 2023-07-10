package lonelymod.fields;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import lonelymod.companions.AbstractCompanion;

@SpirePatch(
        cls="com.megacrit.cardcrawl.characters.AbstractPlayer",
        method=SpirePatch.CLASS
)
public class CompanionField {

    public static SpireField<AbstractCompanion> currCompanion = new SpireField<>(() -> null);
    public static SpireField<AbstractCompanion> hoveredCompanion = new SpireField<>(() -> null);
}
