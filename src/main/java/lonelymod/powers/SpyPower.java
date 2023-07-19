package lonelymod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lonelymod.LonelyMod;
import lonelymod.util.TexLoader;

import static lonelymod.LonelyMod.makeID;

public class SpyPower extends AbstractEasyPower implements CloneablePowerInterface {

    public static final String POWER_ID = makeID("SpyPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/ExampleTwoAmountPower84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/ExampleTwoAmountPower32.png");

    public SpyPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, false, owner, amount);

        this.owner = owner;
        this.amount = amount;

        type = AbstractPower.PowerType.BUFF;
        isTurnBased = false;

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
    public void onSpecificTrigger() {
        if (this.amount > 1)
            addToBot(new ReducePowerAction(this.owner, this.owner, this, this.amount - 1));
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new SpyPower(this.owner, this.amount);
    }
}
