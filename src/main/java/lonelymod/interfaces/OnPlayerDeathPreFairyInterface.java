package lonelymod.interfaces;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

public interface OnPlayerDeathPreFairyInterface {

    boolean onPlayerDeathPreFairy(AbstractPlayer p, DamageInfo info);
}
