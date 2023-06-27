package lonelymod.companions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class Bones {

    public void render(SpriteBatch sb) {
        /*if (!this.isDead && !this.escaped) {
            if (this.atlas == null) {
                sb.setColor(this.tint.color);
                if (this.img != null)
                    sb.draw(this.img, this.drawX - this.img
                    
                        .getWidth() * Settings.scale / 2.0F + this.animX, this.drawY + this.animY, this.img
                        
                        .getWidth() * Settings.scale, this.img
                        .getHeight() * Settings.scale, 0, 0, this.img
                        
                        .getWidth(), this.img
                        .getHeight(), this.flipHorizontal, this.flipVertical); 
            } else {
                this.state.update(Gdx.graphics.getDeltaTime());
                this.state.apply(this.skeleton);
                this.skeleton.updateWorldTransform();
                this.skeleton.setPosition(this.drawX + this.animX, this.drawY + this.animY);
                this.skeleton.setColor(this.tint.color);
                this.skeleton.setFlip(this.flipHorizontal, this.flipVertical);
                sb.end();
                CardCrawlGame.psb.begin();
                sr.draw(CardCrawlGame.psb, this.skeleton);
                CardCrawlGame.psb.end();
                sb.begin();
                sb.setBlendFunction(770, 771);
            } 
            //maybe just make an invisible monster for sneaky gremlin
            /*if (this == (AbstractDungeon.getCurrRoom()).monsters.hoveredMonster && this.atlas == null) {
                sb.setBlendFunction(770, 1);
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.1F));
                if (this.img != null) {
                    sb.draw(this.img, this.drawX - this.img
                    
                        .getWidth() * Settings.scale / 2.0F + this.animX, this.drawY + this.animY, this.img
                        
                        .getWidth() * Settings.scale, this.img
                        .getHeight() * Settings.scale, 0, 0, this.img
                        
                        .getWidth(), this.img
                        .getHeight(), this.flipHorizontal, this.flipVertical);
                    sb.setBlendFunction(770, 771);
                } 
            }
        if (!this.isDying && !this.isEscaping && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.isDead)
            if (!AbstractDungeon.player.hasRelic("Runic Dome") && this.intent != Intent.NONE && !Settings.hideCombatElements) {
                renderIntentVfxBehind(sb);
                renderIntent(sb);
                renderIntentVfxAfter(sb);
                renderDamageRange(sb);
            }  
            this.hb.render(sb);
            this.intentHb.render(sb);
            this.healthHb.render(sb);
        *///} 
        /*if (!AbstractDungeon.player.isDead) {
            renderHealth(sb);
            renderName(sb);
        }*/
    }

    /*public void takeTurn() {
        switch (this.nextMove) {
            case 1: //Basic move
                addToBot(new GainBlockAction(this, this, 3));
                break;
            case 2: //Attack move
                addToBot(new WaitAction(0.5F));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case 3: //Protect move
                addToBot(new GainBlockAction(this, this, 8));
                addToBot(new ApplyPowerAction(this, this, new VigorPower(this, 3), 3));
                break;
            case 4: //Special move
                for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                    addToBot(new ApplyPowerAction(mo, this, new WeakPower(mo, 3, false)));
                    addToBot(new ApplyPowerAction(mo, this, new VulnerablePower(mo, 3, false)));
                }
                addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 3), 3));
                addToBot(new ApplyPowerAction(this, this, new DexterityPower(this, 3), 3));
                break;
        }
        setMove((byte)1, AbstractCompanion.Intent.DEFEND);
    }

    protected void getMove(int paramInt) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMove'");
    }*/
    
}
