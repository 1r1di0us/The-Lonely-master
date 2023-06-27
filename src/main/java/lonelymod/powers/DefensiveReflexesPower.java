package lonelymod.powers;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;

import basemod.interfaces.CloneablePowerInterface;
import lonelymod.LonelyMod;
import lonelymod.util.TexLoader;

public class DefensiveReflexesPower extends AbstractEasyPower implements CloneablePowerInterface {
   
    public static final String POWER_ID = makeID("DefensiveReflexesPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/ExampleTwoAmountPower84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/ExampleTwoAmountPower32.png");

    //private int autoplayAmount;

    public DefensiveReflexesPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, false, owner, amount);

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;
        this.amount = amount;
        //this.autoplayAmount = amount * 3;

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
    public void onCardDraw(AbstractCard card) {
        if (card.cost == -2 || AutoplayField.autoplay.get(card)) {
            addToBot(new GainBlockAction(owner, owner, amount));
        }
        //else if (AutoplayField.autoplay.get(card)) {
        //    addToBot(new GainBlockAction(owner, owner, autoplayAmount));
        //}
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new DefensiveReflexesPower(this.owner, this.amount);
    }
}
