package lonelymod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import lonelymod.fields.CompanionField;
import lonelymod.powers.MechanicPower;

import java.util.Objects;


@SpirePatch(
        clz=ApplyPowerAction.class,
        method=SpirePatch.CONSTRUCTOR,
        paramtypez={
                AbstractCreature.class,
                AbstractCreature.class,
                AbstractPower.class,
                int.class,
                boolean.class,
                AbstractGameAction.AttackEffect.class
        }
)
public class GainLoseFocusPatch {
    public static void Postfix(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
        if (Objects.equals(powerToApply.ID, FocusPower.POWER_ID) && CompanionField.currCompanion.get(AbstractDungeon.player) != null && CompanionField.currCompanion.get(AbstractDungeon.player).hasPower(MechanicPower.POWER_ID)) {
            ((MechanicPower) CompanionField.currCompanion.get(AbstractDungeon.player).getPower(MechanicPower.POWER_ID)).onGainFocus(stackAmount);
        }
    }
}