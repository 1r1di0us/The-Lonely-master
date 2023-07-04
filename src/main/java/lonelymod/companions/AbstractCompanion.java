package lonelymod.companions;

//import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
/*import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;*/
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.DebuffParticleEffect;
import com.megacrit.cardcrawl.vfx.ShieldParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.BuffParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.StunStarEffect;
import com.megacrit.cardcrawl.vfx.combat.UnknownParticleEffect;
import lonelymod.LonelyCharacter;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractCompanion extends AbstractMonster {

    private float intentParticleTimer = 0.0F;
    private BobEffect bobEffect = new BobEffect();
    private ArrayList<AbstractGameEffect> intentVfx = new ArrayList<>();

    //public intentTip = ReflectionHacks.

    /*private Color nameColor = new Color();
    private Color nameBgColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
    private float hoverTimer = 0.0F;
    private float intentAngle = 0.0F;
    private PowerTip intentTip = new PowerTip();
    private Texture intentImg = null;
    private int intentDmg = -1;
    private int intentBaseDmg = -1;
    private int intentMultiAmt = 0;
    private boolean isMultiDmg = false;
    private Color intentColor = Color.WHITE.cpy();*/

    private static final byte BASIC = 0;
    private static final byte ATTACK = 1;
    private static final byte PROTECT = 2;
    private static final byte SPECIAL = 3;

    public AbstractCompanion(String name, String id, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, 1, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
    }
    @Override
    public abstract void takeTurn();

    @Override
    protected abstract void getMove(int i);

    @Override
    public void renderHealth(SpriteBatch sb) {}

    @Override
    public void update() {
        for (AbstractPower p : this.powers)
            p.updateParticles();
        updateReticle();
        updateHealthBar();
        updateAnimations();
        updateIntent();
        this.tint.update();
        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.DEATH) {
            LonelyCharacter.hoveredCompanion = null;
            this.hb.update();
            this.intentHb.update();
            this.healthHb.update();
            if ((this.hb.hovered || this.intentHb.hovered || this.healthHb.hovered) && !AbstractDungeon.player.isDraggingCard) {
                LonelyCharacter.hoveredCompanion = this;
            }
            if (LonelyCharacter.hoveredCompanion == null)
                AbstractDungeon.player.hoverEnemyWaitTimer = -1.0F;
        } else {
            LonelyCharacter.hoveredCompanion = null;
        }
    }


    private void updateIntent() {
        this.bobEffect.update();
        if (this.intentAlpha != this.intentAlphaTarget && this.intentAlphaTarget == 1.0F) {
            this.intentAlpha += Gdx.graphics.getDeltaTime();
            if (this.intentAlpha > this.intentAlphaTarget)
                this.intentAlpha = this.intentAlphaTarget;
        } else if (this.intentAlphaTarget == 0.0F) {
            this.intentAlpha -= Gdx.graphics.getDeltaTime() / 1.5F;
            if (this.intentAlpha < 0.0F)
                this.intentAlpha = 0.0F;
        }
        if (!this.isDying && !this.isEscaping)
            updateIntentVFX();
        for (Iterator<AbstractGameEffect> i = this.intentVfx.iterator(); i.hasNext(); ) {
            AbstractGameEffect e = i.next();
            e.update();
            if (e.isDone)
                i.remove();
        }
    }

    private void updateIntentVFX() {
        if (this.intentAlpha > 0.0F)
            if (this.intent == Intent.ATTACK_DEBUFF || this.intent == Intent.DEBUFF || this.intent == Intent.STRONG_DEBUFF || this.intent == Intent.DEFEND_DEBUFF) {
                this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                if (this.intentParticleTimer < 0.0F) {
                    this.intentParticleTimer = 1.0F;
                    this.intentVfx.add(new DebuffParticleEffect(this.intentHb.cX, this.intentHb.cY));
                }
            } else if (this.intent == Intent.ATTACK_BUFF || this.intent == Intent.BUFF || this.intent == Intent.DEFEND_BUFF) {
                this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                if (this.intentParticleTimer < 0.0F) {
                    this.intentParticleTimer = 0.1F;
                    this.intentVfx.add(new BuffParticleEffect(this.intentHb.cX, this.intentHb.cY));
                }
            } else if (this.intent == Intent.ATTACK_DEFEND) {
                this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                if (this.intentParticleTimer < 0.0F) {
                    this.intentParticleTimer = 0.5F;
                    this.intentVfx.add(new ShieldParticleEffect(this.intentHb.cX, this.intentHb.cY));
                }
            } else if (this.intent == Intent.UNKNOWN) {
                this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                if (this.intentParticleTimer < 0.0F) {
                    this.intentParticleTimer = 0.5F;
                    this.intentVfx.add(new UnknownParticleEffect(this.intentHb.cX, this.intentHb.cY));
                }
            } else if (this.intent == Intent.STUN) {
                this.intentParticleTimer -= Gdx.graphics.getDeltaTime();
                if (this.intentParticleTimer < 0.0F) {
                    this.intentParticleTimer = 0.67F;
                    this.intentVfx.add(new StunStarEffect(this.intentHb.cX, this.intentHb.cY));
                }
            }
    }

    /*@Override
    public void createIntent() {
        this.intent = this.move.intent;
        this.intentParticleTimer = 0.5F;
        this.nextMove = this.move.nextMove;
        this.intentBaseDmg = this.move.baseDamage;
        if (this.move.baseDamage > -1) {
            calculateDamage(this.intentBaseDmg);
            if (this.move.isMultiDamage) {
                this.intentMultiAmt = this.move.multiplier;
                this.isMultiDmg = true;
            } else {
                this.intentMultiAmt = -1;
                this.isMultiDmg = false;
            }
        }
        this.intentImg = getIntentImg();
        this.tipIntent = this.intent;
        this.intentAlpha = 0.0F;
        this.intentAlphaTarget = 1.0F;
        updateIntentTip();
    }*/

    //public abstract void updateIntentTip();

    //the render functions are copied from AbstractMonster.
    //the only change I did is remove the interaction with Runic Dome for both renderTip and render

    //current issue:
    /*java.lang.NullPointerException
	at com.evacipated.cardcrawl.mod.stslib.patches.FlavorText$FlavorTipsAreShorter.Prefix(FlavorText.java:484)
	at com.megacrit.cardcrawl.helpers.TipHelper.getPowerTipHeight(TipHelper.java)
	at com.megacrit.cardcrawl.helpers.TipHelper.getTallestOffset(TipHelper.java:250)
	at com.megacrit.cardcrawl.helpers.TipHelper.calculateAdditionalOffset(TipHelper.java:233)
	at lonelymod.companions.AbstractCompanion.renderTip(AbstractCompanion.java:167)
	at lonelymod.patches.CompanionRenderPatch.Prefix(CompanionRenderPatch.java:19)
*/
    /*@Override
    public void renderTip(SpriteBatch sb) {
        this.tips.clear();
        if (this.intentAlphaTarget == 1.0F && this.intent != AbstractMonster.Intent.NONE) {
            this.tips.add(this.intentTip);
        }

        Iterator var2 = this.powers.iterator();

        while(var2.hasNext()) {
            AbstractPower p = (AbstractPower)var2.next();
            if (p.region48 != null) {
                this.tips.add(new PowerTip(p.name, p.description, p.region48));
            } else {
                this.tips.add(new PowerTip(p.name, p.description, p.img));
            }
        }

        if (!this.tips.isEmpty()) {
            if (this.hb.cX + this.hb.width / 2.0F < TIP_X_THRESHOLD) {
                TipHelper.queuePowerTips(this.hb.cX + this.hb.width / 2.0F + TIP_OFFSET_R_X, this.hb.cY + TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
            } else {
                TipHelper.queuePowerTips(this.hb.cX - this.hb.width / 2.0F + TIP_OFFSET_L_X, this.hb.cY + TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!this.isDead && !this.escaped) {
            if (this.atlas == null) {
                sb.setColor(this.tint.color);
                if (this.img != null) {
                    sb.draw(this.img, this.drawX - (float)this.img.getWidth() * Settings.scale / 2.0F + this.animX, this.drawY + this.animY, (float)this.img.getWidth() * Settings.scale, (float)this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
                }
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

            if (this == AbstractDungeon.getCurrRoom().monsters.hoveredMonster && this.atlas == null) {
                sb.setBlendFunction(770, 1);
                sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.1F));
                if (this.img != null) {// 898
                    sb.draw(this.img, this.drawX - (float)this.img.getWidth() * Settings.scale / 2.0F + this.animX, this.drawY + this.animY, (float)this.img.getWidth() * Settings.scale, (float)this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
                    sb.setBlendFunction(770, 771);
                }
            }

            if (!this.isDying && !this.isEscaping && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.isDead && this.intent != AbstractMonster.Intent.NONE && !Settings.hideCombatElements) {
                this.renderIntentVfxBehind(sb);
                this.renderIntent(sb);
                this.renderIntentVfxAfter(sb);
                this.renderDamageRange(sb);
            }

            this.hb.render(sb);
            this.intentHb.render(sb);
            this.healthHb.render(sb);
        }

        if (!AbstractDungeon.player.isDead) {
            this.renderHealth(sb);
            this.renderName(sb);
        }
    }

    private void renderDamageRange(SpriteBatch sb) {
        if (this.intent.name().contains("ATTACK")) {
            if (this.isMultiDmg) {
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.intentDmg) + "x" + Integer.toString(this.intentMultiAmt), this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + this.bobEffect.y - 12.0F * Settings.scale, this.intentColor);
            } else {
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.intentDmg), this.intentHb.cX - 30.0F * Settings.scale, this.intentHb.cY + this.bobEffect.y - 12.0F * Settings.scale, this.intentColor);
            }
        }
    }

    private void renderIntentVfxBehind(SpriteBatch sb) {
        Iterator var2 = this.intentVfx.iterator();

        while(var2.hasNext()) {
            AbstractGameEffect e = (AbstractGameEffect)var2.next();
            if (e.renderBehind) {
                e.render(sb);
            }
        }
    }

    private void renderIntentVfxAfter(SpriteBatch sb) {
        Iterator var2 = this.intentVfx.iterator();

        while(var2.hasNext()) {
            AbstractGameEffect e = (AbstractGameEffect)var2.next();
            if (!e.renderBehind) {
                e.render(sb);
            }
        }
    }

    private void renderName(SpriteBatch sb) {
        if (!this.hb.hovered) {
            this.hoverTimer = MathHelper.fadeLerpSnap(this.hoverTimer, 0.0F);
        } else {
            this.hoverTimer += Gdx.graphics.getDeltaTime();
        }

        if ((!AbstractDungeon.player.isDraggingCard || AbstractDungeon.player.hoveredCard == null || AbstractDungeon.player.hoveredCard.target == AbstractCard.CardTarget.ENEMY) && !this.isDying) {
            if (this.hoverTimer != 0.0F) {
                if (this.hoverTimer * 2.0F > 1.0F) {
                    this.nameColor.a = 1.0F;
                } else {
                    this.nameColor.a = this.hoverTimer * 2.0F;
                }
            } else {
                this.nameColor.a = MathHelper.slowColorLerpSnap(this.nameColor.a, 0.0F);
            }

            float tmp = Interpolation.exp5Out.apply(1.5F, 2.0F, this.hoverTimer);
            this.nameColor.r = Interpolation.fade.apply(Color.DARK_GRAY.r, Settings.CREAM_COLOR.r, this.hoverTimer * 10.0F);
            this.nameColor.g = Interpolation.fade.apply(Color.DARK_GRAY.g, Settings.CREAM_COLOR.g, this.hoverTimer * 3.0F);
            this.nameColor.b = Interpolation.fade.apply(Color.DARK_GRAY.b, Settings.CREAM_COLOR.b, this.hoverTimer * 3.0F);
            float y = Interpolation.exp10Out.apply(this.healthHb.cY, this.healthHb.cY - 8.0F * Settings.scale, this.nameColor.a);
            float x = this.hb.cX - this.animX;
            this.nameBgColor.a = this.nameColor.a / 2.0F * this.hbAlpha;
            sb.setColor(this.nameBgColor);
            TextureAtlas.AtlasRegion img = ImageMaster.MOVE_NAME_BG;
            sb.draw(img, x - (float)img.packedWidth / 2.0F, y - (float)img.packedHeight / 2.0F, (float)img.packedWidth / 2.0F, (float)img.packedHeight / 2.0F, (float)img.packedWidth, (float)img.packedHeight, Settings.scale * tmp, Settings.scale * 2.0F, 0.0F);
            Color var10000 = this.nameColor;
            var10000.a *= this.hbAlpha;
            FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, this.name, x, y, this.nameColor);
        }
    }

    private void renderIntent(SpriteBatch sb) {
        this.intentColor.a = this.intentAlpha;
        sb.setColor(this.intentColor);*/
        /*if (this.intentBg != null) {
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.intentAlpha / 2.0F));
            if (Settings.isMobile) {
                sb.draw(this.intentBg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale * 1.2F, Settings.scale * 1.2F, 0.0F, 0, 0, 128, 128, false, false);
            } else {
                sb.draw(this.intentBg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
            }
        }*/

        /*if (this.intentImg != null && this.intent != AbstractMonster.Intent.UNKNOWN && this.intent != AbstractMonster.Intent.STUN) {
            if (this.intent != AbstractMonster.Intent.DEBUFF && this.intent != AbstractMonster.Intent.STRONG_DEBUFF) {
                this.intentAngle = 0.0F;
            } else {
                this.intentAngle += Gdx.graphics.getDeltaTime() * 150.0F;
            }

            sb.setColor(this.intentColor);
            if (Settings.isMobile) {
                sb.draw(this.intentImg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale * 1.2F, Settings.scale * 1.2F, this.intentAngle, 0, 0, 128, 128, false, false);
            } else {
                sb.draw(this.intentImg, this.intentHb.cX - 64.0F, this.intentHb.cY - 64.0F + this.bobEffect.y, 64.0F, 64.0F, 128.0F, 128.0F, Settings.scale, Settings.scale, this.intentAngle, 0, 0, 128, 128, false, false);
            }
        }
    }*/
}
