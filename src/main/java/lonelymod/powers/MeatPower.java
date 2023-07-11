package lonelymod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import lonelymod.LonelyMod;
import lonelymod.util.TexLoader;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeID;

public class MeatPower extends AbstractEasyPower implements CloneablePowerInterface {

    public static final String POWER_ID = makeID("MeatPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/ExampleTwoAmountPower84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/ExampleTwoAmountPower32.png");

    private static final int WeakAmt = 1;
    private int turnHp;
    private final ArrayList<AbstractMonster> attackingMons = new ArrayList<AbstractMonster>();

    public AbstractCreature creature;

    public MeatPower(AbstractCreature owner) {
        super(POWER_ID, NAME, AbstractPower.PowerType.BUFF, false, owner, 1);

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;
        this.amount = 1;

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
    public void duringTurn() {
        this.turnHp = AbstractDungeon.player.currentHealth;
        this.attackingMons.clear();
        for (AbstractMonster mon : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!mon.isDeadOrEscaped() && mon.getIntentBaseDmg() >= 0) {
                this.attackingMons.add(mon);
            }
        }
    }

    @Override
    public void onSpecificTrigger() {
        if (AbstractDungeon.player.currentHealth >= this.turnHp && creature instanceof AbstractMonster && this.attackingMons.contains((AbstractMonster) creature))
            addToBot(new ApplyPowerAction(creature, this.owner, new WeakPower(creature, WeakAmt, true), WeakAmt));
        if (AbstractDungeon.player.currentHealth != this.turnHp)
            this.turnHp = AbstractDungeon.player.currentHealth;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public AbstractPower makeCopy() {
        return new MeatPower(this.owner);
    }
}
