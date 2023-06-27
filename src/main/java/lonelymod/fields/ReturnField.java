package lonelymod.fields;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.AbstractCard",
        method=SpirePatch.CLASS
)
public class ReturnField
{
    public static SpireField<Boolean> willReturn = new SpireField<>(() -> false);
}