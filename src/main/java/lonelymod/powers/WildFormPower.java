package lonelymod.powers;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.IntentFlashAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.ShowMoveNameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import basemod.interfaces.CloneablePowerInterface;
import lonelymod.LonelyMod;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;
import lonelymod.util.TexLoader;

public class WildFormPower extends AbstractEasyPower implements CloneablePowerInterface {

    public static final String POWER_ID = makeID("WildFormPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean upgraded;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/ExampleTwoAmountPower84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/ExampleTwoAmountPower32.png");

    public WildFormPower(AbstractCreature owner, int amount, boolean upgraded) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, false, owner, amount);

        this.owner = owner;
        this.upgraded = upgraded;

        type = PowerType.BUFF;
        isTurnBased = false;
        this.amount = amount;

        if (tex84 != null) {
            region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, tex84.getWidth(), tex84.getHeight());
            if (tex32 != null)
                region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, tex32.getWidth(), tex32.getHeight());
        } else if (tex32 != null) {
            this.img = tex32;
            region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, tex32.getWidth(), tex32.getHeight());
        }

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        if (CompanionField.currCompanion.get(AbstractDungeon.player).nextMove != AbstractCompanion.DEFAULT) {
            addToBot(new ShowMoveNameAction(CompanionField.currCompanion.get(AbstractDungeon.player), CompanionField.currCompanion.get(AbstractDungeon.player).moveName));
            addToBot(new IntentFlashAction(CompanionField.currCompanion.get(AbstractDungeon.player)));
            CompanionField.currCompanion.get(AbstractDungeon.player).performTurn();
            CompanionField.currCompanion.get(AbstractDungeon.player).applyTurnPowers();
            CompanionField.currCompanion.get(AbstractDungeon.player).callDefault();
        }
        //CompanionField.currCompanion.get(AbstractDungeon.player).nextMove = AbstractCompanion.NONE;
    }

    @Override
    public void atStartOfTurnPostDraw() {
        flash();
        if (upgraded) {
            addToBot(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, this.amount, false));
        } else {
            addToBot(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, this.amount, true));
        }
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            description = DESCRIPTIONS[0];
        } else if (this.amount > 1) {
            description = DESCRIPTIONS[1];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new WildFormPower(this.owner, this.amount, this.upgraded);
    }
}
