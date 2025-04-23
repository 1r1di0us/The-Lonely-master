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
import lonelymod.interfaces.TriggerOnCallMoveInterface;
import lonelymod.util.TexLoader;

import static lonelymod.LonelyMod.makeID;

public class OutcastPower extends AbstractEasyPower implements CloneablePowerInterface, TriggerOnCallMoveInterface {
    public static final String POWER_ID = makeID("OutcastPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Outcast84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Outcast32.png");

    private final boolean startOfBattle;

    public OutcastPower(AbstractCreature owner, int amount, boolean startOfBattle) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, false, owner, amount);

        this.owner = owner;
        this.amount = amount;
        this.startOfBattle = startOfBattle;

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
        if (amount >= 3 && !startOfBattle)
            description = DESCRIPTIONS[0] + 3 + DESCRIPTIONS[1] + DESCRIPTIONS[2];
        else
            description = DESCRIPTIONS[0] + 3 + DESCRIPTIONS[1];
    }

    @Override
    public void triggerOnCallMove(byte move, byte prevMove) {
        flash();
        this.amount++;
        updateDescription();
    }

    @Override
    public void onSpecificTrigger() {
        flash();
        this.amount = 0;
        updateDescription();
    }

    @Override
    public AbstractPower makeCopy() {
        return new OutcastPower(this.owner, this.amount, this.startOfBattle);
    }
}
