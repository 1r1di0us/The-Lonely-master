package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;

import lonelymod.actions.CallMoveAction;
import lonelymod.actions.SummonOmenAction;
import lonelymod.companions.AbstractCompanion;
import lonelymod.companions.Omen;
import lonelymod.fields.CompanionField;

public class OmenCard extends AbstractEasyCard {
    public final static String ID = makeID("Omen");

    public OmenCard() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = magicNumber = 4;
        this.exhaust = true;
        this.tags.add(Enums.COMPANION);
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //kill current companion, effects, remove strength summon omen (which calls special
        if (CompanionField.currCompanion.get(p) != null && !(CompanionField.currCompanion.get(p) instanceof Omen)) {
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, -magicNumber)));
            addToBot(new DamageAction(CompanionField.currCompanion.get(AbstractDungeon.player), new DamageInfo(p, 1, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            //copied from Corruption:
            addToBot(new SFXAction("VO_CULTIST_2C"));
            addToBot(new VFXAction(p, new VerticalAuraEffect(Color.BLACK, p.hb.cX, p.hb.cY), 0.1F));
            addToBot(new VFXAction(p, new VerticalAuraEffect(Color.CYAN, p.hb.cX, p.hb.cY), 0.0F));
            addToBot(new VFXAction(p, new BorderLongFlashEffect(Color.BLACK), 0.0F, true));
            addToBot(new SummonOmenAction(false));
        } else {
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, -magicNumber)));
            //copied from Corruption:
            addToBot(new VFXAction(p, new VerticalAuraEffect(Color.BLACK, p.hb.cX, p.hb.cY), 0.1F));
            addToBot(new VFXAction(p, new VerticalAuraEffect(Color.CYAN, p.hb.cX, p.hb.cY), 0.0F));
            addToBot(new VFXAction(p, new BorderLongFlashEffect(Color.BLACK), 0.0F, true));

            addToBot(new CallMoveAction(AbstractCompanion.SPECIAL, CompanionField.currCompanion.get(AbstractDungeon.player)));

        }
        //addToBot(new CallMoveAction(AbstractCompanion.SPECIAL, CompanionField.currCompanion.get(AbstractDungeon.player)));
    }

    public void upp() {
        upgradeMagicNumber(-2);
    }
}
