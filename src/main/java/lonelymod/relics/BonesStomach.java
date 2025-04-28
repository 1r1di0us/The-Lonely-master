package lonelymod.relics;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.cards.AbstractCard;
import lonelymod.LonelyCharacter;
import lonelymod.actions.SummonCompanionAction;
import lonelymod.cards.summonmoves.*;
import lonelymod.companions.Bones;

import java.util.ArrayList;

public class BonesStomach extends AbstractEasyRelic {
    public static final String ID = makeID("BonesStomach");

    public BonesStomach() {
        super(ID, RelicTier.STARTER, LandingSound.FLAT, LonelyCharacter.Enums.YELLOW);
        cardToPreview.addAll(CardTips);
    }

    @Override
    public void atPreBattle() {
        flash();
        addToBot(new SummonCompanionAction(new Bones()));
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
