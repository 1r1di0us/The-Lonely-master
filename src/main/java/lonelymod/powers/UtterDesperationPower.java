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

public class UtterDesperationPower extends AbstractEasyPower implements CloneablePowerInterface, AtEndOfTurnPostEndTurnCardsInterface {

    public static final String POWER_ID = makeID("UtterDesperationPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/ExampleTwoAmountPower84.png");
    private static final Texture tex32 = TexLoader.getTexture(LonelyMod.modID + "Resources/images/powers/ExampleTwoAmountPower32.png");

    private CardGroup previousHand;

    public UtterDesperationPower(AbstractCreature owner, int amount) {
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

    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if (isPlayer) {
            if (!AbstractDungeon.player.hand.isEmpty()) {
                previousHand = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (!c.isEthereal) {
                        previousHand.addToTop(c);
                    }
                }
            }
        }
    }

    @Override
    public void atEndOfTurnPostEndTurnCards(boolean isPlayer) {
        if (isPlayer) {
            for (int i = 0; i < this.amount; i++) {
                AbstractCard cardToPlay = previousHand.getRandomCard(true);
                for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                    if (Objects.equals(cardToPlay.cardID, c.cardID)) {
                        cardToPlay = c;
                        break;
                    }
                }
                //cardToPlay.freeToPlayOnce = true;
                addToBot(new PlayCardAction(AbstractDungeon.player.hand, cardToPlay, AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), false));
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
        return new UtterDesperationPower(this.owner, this.amount);
    }

}
