package lonelymod.powers;

import static lonelymod.ModFile.makeID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;

import basemod.interfaces.CloneablePowerInterface;
import lonelymod.ModFile;
import lonelymod.actions.CompanionAttackAbilityAction;
import lonelymod.util.TexLoader;

public class AnimalSavageryPower extends AbstractEasyPower implements CloneablePowerInterface {
   
    public static final String POWER_ID = makeID("AnimalSavageryPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(ModFile.modID + "Resources/images/powers/ExampleTwoAmountPower84.png");
    private static final Texture tex32 = TexLoader.getTexture(ModFile.modID + "Resources/images/powers/ExampleTwoAmountPower32.png");

    private boolean turnStart = false;

    public AnimalSavageryPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, true, owner, amount);

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = true;
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
    public void atStartOfTurnPostDraw() {
        turnStart = true;
    }

    @Override
    public void onChannel(AbstractOrb orb) {
        if (turnStart == true) {
            flash();
            if (this.amount > 1) {
                AbstractDungeon.actionManager.addToBottom(new CompanionAttackAbilityAction());
                this.amount -= 1;
            }
            else if (this.amount <= 1)
                addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this));
            turnStart = false;
        }
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else if (amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new AnimalSavageryPower(this.owner, this.amount);
    }
}
