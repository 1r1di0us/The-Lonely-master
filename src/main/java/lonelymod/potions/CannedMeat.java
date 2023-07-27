package lonelymod.potions;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import lonelymod.actions.CallMoveAction;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;
import lonelymod.powers.CompanionDexterityPower;

import static lonelymod.LonelyMod.makeID;

public class CannedMeat extends AbstractPotion {
    public static final String POTION_ID = makeID("CannedMeat");

    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public CannedMeat() {
        super(potionStrings.NAME, POTION_ID, AbstractPotion.PotionRarity.COMMON, PotionSize.JAR, AbstractPotion.PotionColor.STRENGTH);
        this.labOutlineColor = Settings.LIGHT_YELLOW_COLOR;
        this.isThrown = false;
    }

    public void initializeData() {
        this.potency = getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + this.potency + potionStrings.DESCRIPTIONS[1] + this.potency + potionStrings.DESCRIPTIONS[2];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(

                TipHelper.capitalize(GameDictionary.STRENGTH.NAMES[0]), GameDictionary.keywords
                .get(GameDictionary.STRENGTH.NAMES[0])));
        this.tips.add(new PowerTip(

                TipHelper.capitalize(GameDictionary.DEXTERITY.NAMES[0]), GameDictionary.keywords
                .get(GameDictionary.DEXTERITY.NAMES[0])));
    }

    public void use(AbstractCreature target) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) == null) {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, CallMoveAction.TEXT[0], true));
        } else {
            AbstractCompanion companion = CompanionField.currCompanion.get(AbstractDungeon.player);
            if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
                addToBot(new ApplyPowerAction(companion, AbstractDungeon.player, new StrengthPower(companion, this.potency), this.potency));
                addToBot(new ApplyPowerAction(companion, AbstractDungeon.player, new CompanionDexterityPower(companion, this.potency), this.potency));
            }
        }
    }

    public int getPotency(int ascensionLevel) {
        return 1;
    }

    public AbstractPotion makeCopy() {
        return new CannedMeat();
    }
}
