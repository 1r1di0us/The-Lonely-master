package lonelymod.potions;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import lonelymod.KeywordManager;
import lonelymod.actions.CallMoveAction;
import lonelymod.actions.CompanionTakeTurnAction;
import lonelymod.actions.PerformMoveAction;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;

import static lonelymod.LonelyMod.makeID;

public class SpecialSauce extends AbstractPotion {
    public static final String POTION_ID = makeID("SpecialSauce");

    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public SpecialSauce() {
        super(potionStrings.NAME, POTION_ID, AbstractPotion.PotionRarity.RARE, PotionSize.S, PotionColor.ANCIENT);
        this.labOutlineColor = Settings.LIGHT_YELLOW_COLOR;
        this.isThrown = false;
    }

    public void initializeData() {
        this.potency = getPotency();
        if (this.potency == 1) this.description = potionStrings.DESCRIPTIONS[0];
        else this.description = potionStrings.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(
                KeywordManager.KEYWORDS.get("special_move").PROPER_NAME,KeywordManager.KEYWORDS.get("special_move").DESCRIPTION));
    }

    public void use(AbstractCreature target) {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) == null) {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
        }
        else {
            byte prevMove = CompanionField.currCompanion.get(AbstractDungeon.player).nextMove;
            for (int i = 0; i < this.potency; i++) {
                addToBot(new PerformMoveAction(AbstractCompanion.SPECIAL, CompanionField.currCompanion.get(AbstractDungeon.player))); //this way we don't show the name
                addToBot(new WaitAction(0.1F));
            }
            addToBot(new CallMoveAction(prevMove, CompanionField.currCompanion.get(AbstractDungeon.player), false, false)); // hopefully no one noticed
        }
    }

    public int getPotency(int ascensionLevel) {
        return 1;
    }

    public AbstractPotion makeCopy() {
        return new SpecialSauce();
    }
}
