package lonelymod.cards.cardvars;

import basemod.abstracts.DynamicVariable;
import lonelymod.cards.AbstractEasyCard;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.cards.AbstractCard;

public class ThirdMagicNumber extends DynamicVariable {

    @Override
    public String key() {
        return makeID("m3");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractEasyCard) {
            return ((AbstractEasyCard) card).isThirdMagicModified;
        }
        return false;
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractEasyCard) {
            return ((AbstractEasyCard) card).thirdMagic;
        }
        return -1;
    }

    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof AbstractEasyCard) {
            ((AbstractEasyCard) card).isThirdMagicModified = v;
        }
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractEasyCard) {
            return ((AbstractEasyCard) card).baseThirdMagic;
        }
        return -1;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractEasyCard) {
            return ((AbstractEasyCard) card).upgradedThirdMagic;
        }
        return false;
    }
}