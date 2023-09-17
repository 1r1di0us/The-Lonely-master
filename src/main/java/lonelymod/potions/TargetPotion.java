package lonelymod.potions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import lonelymod.KeywordManager;
import lonelymod.powers.TargetPower;

import static lonelymod.LonelyMod.makeID;

public class TargetPotion extends AbstractPotion {
    public static final String POTION_ID = makeID("TargetPotion");

    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    private static final int powerAmount = 5;

    public TargetPotion() {
        super(potionStrings.NAME, POTION_ID, AbstractPotion.PotionRarity.COMMON, PotionSize.SPIKY, PotionColor.EXPLOSIVE);
        this.labOutlineColor = Settings.LIGHT_YELLOW_COLOR;
        this.isThrown = true;
        this.targetRequired = true;
    }

    public void initializeData() {
        this.potency = getPotency();
        this.description = potionStrings.DESCRIPTIONS[0] + (powerAmount * this.potency) + potionStrings.DESCRIPTIONS[1];        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(
                KeywordManager.KEYWORDS.get("target").PROPER_NAME, KeywordManager.KEYWORDS.get("target").DESCRIPTION));
    }

    public void use(AbstractCreature target) {
        addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new TargetPower(target, powerAmount * this.potency, false)));
    }

    public int getPotency(int ascensionLevel) {
        return 1;
    }

    public AbstractPotion makeCopy() {
        return new TargetPotion();
    }
}
