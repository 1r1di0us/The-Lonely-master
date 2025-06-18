package lonelymod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lonelymod.LonelyMod;
import lonelymod.util.TexLoader;

import static lonelymod.LonelyMod.makeID;

public class AddCardToHandPower extends AbstractEasyPower implements CloneablePowerInterface, NonStackablePower {

    public static final String POWER_ID = makeID("AddCardToHandPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/AddCardToHand84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/AddCardToHand32.png");

    private final AbstractCard cardToAdd;
    private final boolean isFree;

    public AddCardToHandPower(AbstractCreature owner, int amount, AbstractCard cardToAdd, boolean isFree) {
        super(POWER_ID, cardToAdd.name + NAME, AbstractPower.PowerType.BUFF, true, owner, amount);

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = true;
        this.amount = amount;
        this.cardToAdd = cardToAdd;
        this.isFree = isFree;

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
    public void atStartOfTurn() {
        flash();
        if (isFree) {
            this.cardToAdd.freeToPlayOnce = true;
        }
        addToBot(new MakeTempCardInHandAction(this.cardToAdd, this.amount));
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public boolean isStackable(AbstractPower power) {
        return power instanceof AddCardToHandPower
                && ((AddCardToHandPower) power).getCardToAdd().getClass() == this.getCardToAdd().getClass()
                && ((AddCardToHandPower) power).getIsFree() == this.getIsFree();
    }

    public AbstractCard getCardToAdd() {
        return cardToAdd;
    }

    public boolean getIsFree() { return isFree; }

    @Override
    public void updateDescription() {
        if (cardToAdd == null) {
            description = DESCRIPTIONS[4];
        } else if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + cardToAdd.name.replaceAll(" ", " #y") + DESCRIPTIONS[2];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + cardToAdd.name.replaceAll(" ", " #y") + DESCRIPTIONS[3];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new AddCardToHandPower(this.owner, this.amount, this.cardToAdd, this.isFree);
    }
}
