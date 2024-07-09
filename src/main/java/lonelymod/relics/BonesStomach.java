package lonelymod.relics;

import static lonelymod.LonelyMod.makeID;

import basemod.helpers.CardPowerTip;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.PowerTip;
import lonelymod.LonelyCharacter;
import lonelymod.actions.SummonBonesAction;
import lonelymod.cards.summonmoves.*;

import java.util.ArrayList;

public class BonesStomach extends AbstractEasyRelic {
    public static final String ID = makeID("BonesStomach");

    public BonesStomach() {
        super(ID, RelicTier.STARTER, LandingSound.FLAT, LonelyCharacter.Enums.YELLOW);
        cardToPreview.addAll(CardTips);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new SummonBonesAction());
    }

    public static final ArrayList<AbstractCard> CardTips = new ArrayList<AbstractCard>() {
        {
            add(new Excitement());
            add(new Bark());
            add(new Bite());
            add(new Poise());
            add(new Howl());
        }
    };
}
