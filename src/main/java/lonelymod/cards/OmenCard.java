package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;

import lonelymod.actions.CallMoveAction;
import lonelymod.actions.SummonOmenCardAction;
import lonelymod.cards.summonmoves.*;
import lonelymod.companions.AbstractCompanion;
import lonelymod.companions.Omen;
import lonelymod.fields.CompanionField;

import java.util.ArrayList;

public class OmenCard extends AbstractEasyCard {
    public final static String ID = makeID("Omen");

    public OmenCard() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        this.baseMagicNumber = magicNumber = 4;
        this.exhaust = true;
        this.tags.add(Enums.COMPANION);

        this.cardToPreview.addAll(CardTips);
    }

    public static final ArrayList<AbstractCard> CardTips = new ArrayList<AbstractCard>() {
        {
            add(new Claws());
            add(new Peck());
            add(new Shred());
            add(new Shriek());
            add(new Sharpen());
        }
    };

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //remove strength, kill current companion, effects, summon omen, call special
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, -magicNumber)));
        if (!(CompanionField.currCompanion.get(p) instanceof Omen)) {
            //addToBot(new DamageAction(CompanionField.currCompanion.get(AbstractDungeon.player), new DamageInfo(p, 0, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HEAVY));
            addToBot(new EscapeAction(CompanionField.currCompanion.get(AbstractDungeon.player)));
            //copied from Corruption:
            addToBot(new SFXAction("VO_CULTIST_2C"));
            addToBot(new VFXAction(p, new VerticalAuraEffect(Color.BLACK, p.hb.cX, p.hb.cY), 0.1F));
            addToBot(new VFXAction(p, new VerticalAuraEffect(Color.CYAN, p.hb.cX, p.hb.cY), 0.0F));
            addToBot(new VFXAction(p, new BorderLongFlashEffect(Color.BLACK), 0.0F, true));
            addToBot(new SummonOmenCardAction(false));
        } else { //omen already summoned
            //copied from Corruption:
            addToBot(new VFXAction(p, new VerticalAuraEffect(Color.BLACK, p.hb.cX, p.hb.cY), 0.1F));
            addToBot(new VFXAction(p, new VerticalAuraEffect(Color.CYAN, p.hb.cX, p.hb.cY), 0.0F));
            addToBot(new VFXAction(p, new BorderLongFlashEffect(Color.BLACK), 0.0F, true));
            addToBot(new CallMoveAction(AbstractCompanion.SPECIAL, CompanionField.currCompanion.get(AbstractDungeon.player)));

        }
    }

    public void upp() {
        upgradeMagicNumber(-1);
    }
}
