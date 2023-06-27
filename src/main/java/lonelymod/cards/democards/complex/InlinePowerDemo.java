package lonelymod.cards.democards.complex;

import static lonelymod.LonelyMod.makeID;
import static lonelymod.util.Wiz.applyToSelf;
import static lonelymod.util.Wiz.atb;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import lonelymod.cards.AbstractEasyCard;
import lonelymod.powers.LambdaPower;

public class InlinePowerDemo extends AbstractEasyCard {

    public final static String ID = makeID(InlinePowerDemo.class.getSimpleName());
    // intellij stuff power, self, uncommon

    private static final int MAGIC = 4;
    private static final int UPG_MAGIC = 2;

    public InlinePowerDemo() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = MAGIC;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new LambdaPower(makeID("StrikeAndBlockBoostPower"), cardStrings.EXTENDED_DESCRIPTION[0], AbstractPower.PowerType.BUFF, false, p, magicNumber) {
            @Override
            public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
                if (card.hasTag(CardTags.STRIKE)) {
                    return damage + amount;
                }
                return damage;
            }

            @Override
            public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
                flash();
                atb(new GainBlockAction(owner, amount));
            }

            @Override
            public void updateDescription() {
                description = cardStrings.EXTENDED_DESCRIPTION[1] + amount + cardStrings.EXTENDED_DESCRIPTION[2] + amount + cardStrings.EXTENDED_DESCRIPTION[3];
            }
        });
    }

    public void upp() {
        upgradeMagicNumber(UPG_MAGIC);
    }
} 