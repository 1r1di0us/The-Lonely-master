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
import lonelymod.LonelyCharacter;
import lonelymod.actions.WaterFlaskAction;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;
import lonelymod.powers.CompanionDexterityPower;

import static lonelymod.LonelyMod.makeID;

public class WaterFlask extends AbstractPotion {
    public static final String POTION_ID = makeID("WaterFlask");

    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final int drawAmount = 2;

    public WaterFlask() {
        super(potionStrings.NAME, POTION_ID, AbstractPotion.PotionRarity.UNCOMMON, PotionSize.EYE, PotionColor.BLUE);
        this.labOutlineColor = Settings.LIGHT_YELLOW_COLOR;
        this.isThrown = false;
    }

    public void initializeData() {
        this.potency = getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + (drawAmount * this.potency) + potionStrings.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(

                TipHelper.capitalize(GameDictionary.STATUS.NAMES[0]), GameDictionary.keywords
                .get(GameDictionary.STATUS.NAMES[0])));
    }

    public void use(AbstractCreature target) {
        addToBot(new WaterFlaskAction(drawAmount * this.potency));
    }

    public int getPotency(int ascensionLevel) {
        return 1;
    }

    public AbstractPotion makeCopy() {
        return new WaterFlask();
    }
}
