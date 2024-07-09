package lonelymod.relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.CardPowerTip;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.helpers.PowerTip;
import lonelymod.util.TexLoader;

import static lonelymod.LonelyMod.makeRelicPath;
import static lonelymod.LonelyMod.modID;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public abstract class AbstractEasyRelic extends CustomRelic {
    public AbstractCard.CardColor color;

    protected AbstractCard cardsToPreview = null; //naming them opposite since that's the way they are in AbstractCard
    protected ArrayList<AbstractCard> cardToPreview = new ArrayList<>();
    protected int previewIndex = 0;
    protected float rotationTimer = 0.f;

    public AbstractEasyRelic(String setId, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        this(setId, tier, sfx, null);
    }

    public AbstractEasyRelic(String setId, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx, AbstractCard.CardColor color) {
        super(setId, TexLoader.getTexture(makeRelicPath(setId.replace(modID + ":", "") + ".png")), tier, sfx);
        outlineImg = TexLoader.getTexture(makeRelicPath(setId.replace(modID + ":", "") + "Outline.png"));
        this.color = color;
    }

    @Override
    public void update() {
        super.update();
        if (!cardToPreview.isEmpty()) {
            if (hb.hovered) {
                if (rotationTimer <= 0F) {
                    rotationTimer = getRotationTimeNeeded();
                    cardsToPreview = cardToPreview.get(previewIndex);
                    if (previewIndex == cardToPreview.size() - 1) {
                        previewIndex = 0;
                    } else {
                        previewIndex++;
                    }
                } else {
                    rotationTimer -= Gdx.graphics.getDeltaTime();
                }
            }
        }
        if (cardsToPreview != null) refreshTips();
    }

    public void refreshTips() {
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
        this.tips.add(new CardPowerTip(cardsToPreview));
    }

    protected float getRotationTimeNeeded() {
        return 2.5f;
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}