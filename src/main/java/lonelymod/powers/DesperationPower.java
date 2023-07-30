package lonelymod.powers;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import basemod.interfaces.CloneablePowerInterface;
import lonelymod.LonelyMod;
import lonelymod.actions.PlayCardAction;
import lonelymod.interfaces.AtEndOfTurnPostEndTurnCardsInterface;
import lonelymod.util.TexLoader;

import java.util.Objects;

public class DesperationPower extends AbstractEasyPower implements CloneablePowerInterface {

    public static final String POWER_ID = makeID("DesperationPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Desperation84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/Desperation32.png");

    public DesperationPower(AbstractCreature owner, int amount) {
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
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if (isPlayer) {
            for (int i = 0; i < this.amount; i++) {
                if (!AbstractDungeon.player.discardPile.isEmpty()) {
                    AbstractCard cardToPlay = AbstractDungeon.player.discardPile.getRandomCard(true);
                    addToBot(new PlayCardAction(AbstractDungeon.player.discardPile, cardToPlay, AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), true));
                }
            }
        }
    }

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
        } else if (this.amount > 1) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new DesperationPower(this.owner, this.amount);
    }

}
