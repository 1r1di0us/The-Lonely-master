package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;

import lonelymod.actions.AutoplayWaitAction;
import lonelymod.actions.CompanionProtectAbilityAction;
import lonelymod.powers.OldOmenPower;

public class Omen extends AbstractEasyCard {
    public final static String ID = makeID("Omen");

    public Omen() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = magicNumber = 3;
        this.baseSecondMagic = secondMagic = 1;
        this.exhaust = true;
        AutoplayField.autoplay.set(this, true);
        this.cardsToPreview = (AbstractCard) new Primal();
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //copied from Corruption:
        addToBot((AbstractGameAction)new VFXAction((AbstractCreature)p, (AbstractGameEffect)new VerticalAuraEffect(Color.BLACK, p.hb.cX, p.hb.cY), 0.33F));
        addToBot((AbstractGameAction)new SFXAction("VO_CULTIST_2C"));
        addToBot((AbstractGameAction)new VFXAction((AbstractCreature)p, (AbstractGameEffect)new VerticalAuraEffect(Color.PURPLE, p.hb.cX, p.hb.cY), 0.33F));
        addToBot((AbstractGameAction)new VFXAction((AbstractCreature)p, (AbstractGameEffect)new VerticalAuraEffect(Color.CYAN, p.hb.cX, p.hb.cY), 0.0F));
        addToBot((AbstractGameAction)new VFXAction((AbstractCreature)p, (AbstractGameEffect)new BorderLongFlashEffect(Color.MAGENTA), 0.0F, true));
        //discard, remove focus, apply omen power, make primal instincts, call protect
        AbstractDungeon.actionManager.addToBottom(new DiscardAction(p, p, 3, true));
        if (p.hasPower("Focus"))
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, p.getPower("Focus")));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new OldOmenPower(p, 6), 6));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard) new Primal(), 1, true, true));
        if (upgraded) {
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction((AbstractCard) new Primal(), 1, true, true));
        }
        AbstractDungeon.actionManager.addToBottom(new CompanionProtectAbilityAction());
        AbstractDungeon.actionManager.addToBottom(new AutoplayWaitAction(1.0f));
    }

    public void upp() {
        upgradeSecondMagic(1);
        uDesc();
    }
}
