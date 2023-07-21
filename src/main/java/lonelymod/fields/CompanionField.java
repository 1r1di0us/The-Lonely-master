package lonelymod.fields;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import lonelymod.companions.AbstractCompanion;

import java.util.ArrayList;

@SpirePatch(
        cls="com.megacrit.cardcrawl.characters.AbstractPlayer",
        method=SpirePatch.CLASS
)
public class CompanionField {

    public static SpireField<AbstractCompanion> currCompanion = new SpireField<>(() -> null);
    public static SpireField<AbstractCompanion> hoveredCompanion = new SpireField<>(() -> null);
    public static SpireField<ArrayList<AbstractCard>> playableCards = new SpireField<>(() -> null);
}
