package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;

public class OccultPractice extends AbstractEasyCard {
    public final static String ID = makeID("OccultPractice");

    public OccultPractice() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 4;
        baseSecondMagic = secondMagic = 1;
        this.cardsToPreview = new Wound();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(p, new VerticalAuraEffect(Color.BLUE, p.hb.cX, p.hb.cY), 0.33F));
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, this.magicNumber), this.magicNumber));
        addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, this.magicNumber), this.magicNumber));
        addToBot(new MakeTempCardInDiscardAction(new Wound(), this.secondMagic));
    }
    

    public void upp() {
        upgradeMagicNumber(2);
    }
}
