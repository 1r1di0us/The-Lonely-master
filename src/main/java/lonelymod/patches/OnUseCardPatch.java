package lonelymod.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lonelymod.fields.CompanionField;

@SpirePatch(
        clz = UseCardAction.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {
                AbstractCard.class,
                AbstractCreature.class
        }
)
public class OnUseCardPatch {
    public static void Postfix(UseCardAction __instance, AbstractCard card, AbstractCreature target) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            for (AbstractPower p : CompanionField.currCompanion.get(AbstractDungeon.player).powers) {
                if (!card.dontTriggerOnUseCard) {
                    p.onUseCard(card, __instance);
                }
            }
        }
    }
}
