package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;

import lonelymod.actions.AutoplayWaitAction;
import lonelymod.actions.SummonOmenAction;

public class OmenCard extends AbstractEasyCard {
    public final static String ID = makeID("Omen");

    public OmenCard() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = magicNumber = 5;
        this.baseSecondMagic = secondMagic = 1;
        this.exhaust = true;
        AutoplayField.autoplay.set(this, true);
        this.cardsToPreview = new Primal();
        this.tags.add(Enums.COMPANION);
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //copied from Corruption:
        addToBot(new VFXAction(p, new VerticalAuraEffect(Color.BLACK, p.hb.cX, p.hb.cY), 0.33F));
        addToBot(new SFXAction("VO_CULTIST_2C"));
        addToBot(new VFXAction(p, new VerticalAuraEffect(Color.PURPLE, p.hb.cX, p.hb.cY), 0.33F));
        addToBot(new VFXAction(p, new VerticalAuraEffect(Color.CYAN, p.hb.cX, p.hb.cY), 0.0F));
        addToBot(new VFXAction(p, new BorderLongFlashEffect(Color.MAGENTA), 0.0F, true));
        //remove strength, kill current companion, summon omen, make primal instincts, call protect
        addToBot(new AutoplayWaitAction(1.0f));
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, -magicNumber)));
        addToBot(new SummonOmenAction(false));
        addToBot(new MakeTempCardInDrawPileAction( new Primal(), 1, true, true));
        if (upgraded) {
            addToBot(new MakeTempCardInDrawPileAction( new Primal(), 1, true, true));
        }
    }

    public void upp() {
        upgradeSecondMagic(1);
        uDesc();
    }
}
