package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.vfx.combat.HemokinesisEffect;

public class DarkRitual extends AbstractEasyCard {
    public final static String ID = makeID("DarkRitual");

    public DarkRitual() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 3;
        this.baseSecondMagic = this.secondMagic = 1;
        this.exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new HemokinesisEffect(p.hb.cX, p.hb.cY, p.hb.cX, p.hb.cY), 0.5F));
        addToBot(new LoseHPAction(p, p, this.magicNumber));
        addToBot(new ApplyPowerAction(p, p, new RitualPower(p, secondMagic, true)));
    }

    public void upp() {
        this.isInnate = true;
        uDesc();
    }
}
