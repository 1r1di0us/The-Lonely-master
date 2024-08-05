package lonelymod.relics.deprecated;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import lonelymod.LonelyCharacter;
import lonelymod.relics.AbstractEasyRelic;

import static lonelymod.LonelyMod.makeID;

public class DEPRECATEDBookOfTheLaw extends AbstractEasyRelic {
    public static final String ID = makeID("BookOfTheLaw");

    private static final int DMG = 1;

    public DEPRECATEDBookOfTheLaw() {
        super(ID, RelicTier.BOSS, LandingSound.HEAVY, LonelyCharacter.Enums.YELLOW);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DMG + DESCRIPTIONS[1];
    }

    @Override
    public void onPlayerEndTurn() {
        if (!AbstractDungeon.player.hand.group.isEmpty()) {
            flash();
            addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(null, AbstractDungeon.player.hand.group.size() * DMG, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }

    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
    }

    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }
}
