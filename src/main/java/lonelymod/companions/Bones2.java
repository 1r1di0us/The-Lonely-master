package lonelymod.companions;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import hlysine.friendlymonsters.monsters.AbstractFriendlyMonster;
import hlysine.friendlymonsters.monsters.MinionMove;

import static lonelymod.LonelyMod.makeCompanionPath;
import static lonelymod.LonelyMod.makeID;

public class Bones2 extends AbstractFriendlyMonster {
    public static final String NAME = "Bones";
    public static final String ID = makeID("Bones");
    public static final int MAX_HP = 1;
    public static final String IMG = makeCompanionPath("Bones.png");

    public Bones2(float drawX, float drawY) {
        super(NAME, ID, MAX_HP, 0, 0, 200, 200, IMG, drawX, drawY);

        addMove(new MinionMove("Basic", this,
                new Texture("images/ui/intent/defend.png"),
                "Deal 3 damage",
                () -> {
                    AbstractMonster target = AbstractDungeon.getRandomMonster();
                    DamageInfo info = new DamageInfo(this, 3, DamageInfo.DamageType.NORMAL);
                    info.applyPowers(this, target); // <--- This lets powers effect minions attacks
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(target, info));
                }
        ));
    }

    /*@Override
    public void renderHealth(SpriteBatch sb) {}*/
}
