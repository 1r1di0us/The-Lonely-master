package lonelymod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lonelymod.LonelyMod;
import lonelymod.util.TexLoader;

import static lonelymod.LonelyMod.makeID;

public class OutcastPower extends AbstractEasyPower implements CloneablePowerInterface, NonStackablePower {
    public static final String POWER_ID = makeID("OutcastPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/ExampleTwoAmountPower84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/ExampleTwoAmountPower32.png");

    public OutcastPower(AbstractCreature owner) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, false, owner, 1);

        this.owner = owner;

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
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    public void updateDescription(int conAttack, int conProtect, int conSpecial) {
        //probably an easier way of doing this but i don't care.
        if (conAttack == 3) {
            if (conProtect == 3) {
                if (conSpecial == 3) {
                    description = DESCRIPTIONS[0] + DESCRIPTIONS[2] + DESCRIPTIONS[1] + DESCRIPTIONS[3] + DESCRIPTIONS[1] + DESCRIPTIONS[4] + DESCRIPTIONS[6];
                } else {
                    description = DESCRIPTIONS[0] + DESCRIPTIONS[2] + DESCRIPTIONS[1] + DESCRIPTIONS[3] + DESCRIPTIONS[6];
                }
            } else {
                if (conSpecial == 3) {
                    description = DESCRIPTIONS[0] + DESCRIPTIONS[2] + DESCRIPTIONS[1] + DESCRIPTIONS[4] + DESCRIPTIONS[6];
                } else {
                    description = DESCRIPTIONS[0] + DESCRIPTIONS[2] + DESCRIPTIONS[6];
                }
            }
        } else {
            if (conProtect == 3) {
               if (conSpecial == 3) {
                   description = DESCRIPTIONS[0] + DESCRIPTIONS[3] + DESCRIPTIONS[1] + DESCRIPTIONS[4] + DESCRIPTIONS[6];
               } else {
                   description = DESCRIPTIONS[0] + DESCRIPTIONS[3] + DESCRIPTIONS[6];
               }
            } else {
                if (conSpecial == 3) {
                    description = DESCRIPTIONS[0] + DESCRIPTIONS[4] + DESCRIPTIONS[6];
                } else {
                    description = DESCRIPTIONS[0] + DESCRIPTIONS[5] + DESCRIPTIONS[6];
                }
            }
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new OutcastPower(this.owner);
    }
}
